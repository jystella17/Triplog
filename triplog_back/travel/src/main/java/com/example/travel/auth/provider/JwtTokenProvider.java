package com.example.travel.auth.provider;

import com.example.travel.auth.entity.Token;
import com.example.travel.auth.entity.User;
import com.example.travel.auth.entity.UserPrincipal;
import com.example.travel.auth.repository.RedisRepository;
import com.example.travel.auth.repository.TokenRepository;
import com.example.travel.auth.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider { // 유효한 JWT Token 생성

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_NAME;

    @Value("${app.auth.token.access-expiration-time}")
    private Long ACCESS_TOKEN_EXPIRE_LENGTH; // 12 hours

    @Value("${app.auth.token.refresh-expiration-time}")
    private Long REFRESH_TOKEN_EXPIRE_LENGTH;	// 1week
    private static final String AUTHORITIES_KEY = "role";
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(@Value("${app.auth.token.secret-key}") final String secretKey,
                            @Value("${app.auth.token.refresh-cookie-name}") String cookieName,
                            TokenRepository tokenRepository, UserRepository userRepository,
                            RedisRepository redisRepository, ObjectMapper objectMapper) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_NAME = cookieName;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.redisRepository = redisRepository;
        this.objectMapper = objectMapper;
    }

    public String createAcessToken(Authentication authentication) {
        Date now = new Date();
        Date valid_until = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String uniqueId = userPrincipal.getUniqueId();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(uniqueId)
                .claim(AUTHORITIES_KEY, role)
                .setIssuer("travel")
                .setIssuedAt(now)
                .setExpiration(valid_until)
                .compact();
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response) throws Exception {
        Date now = new Date();
        Date valid_until = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuer("travel-refresh")
                .setIssuedAt(now)
                .setExpiration(valid_until)
                .compact();

        saveRefreshToken(authentication, refreshToken);
        log.info("Refresh Token 발급: " + authentication.getName());

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_NAME, refreshToken)
                .httpOnly(true).secure(true).sameSite("Lax")
                .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH/1000)
                .path("/").build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String uniqueId = userPrincipal.getUniqueId();

        Optional<User> targetUser = userRepository.findUserByUniqueId(uniqueId);
        if(targetUser.isEmpty()) {
            throw new Exception("Cannot find user");
        }

        Token refresh = Token.builder().email(targetUser.get().getEmail()).refreshToken(refreshToken).build();
        tokenRepository.save(refresh); // 새로 발급받은 토큰 저장
    }

    // Request에 포함된 JWT Token (Access Token)으로 인증 정보 조회 및 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Optional<User> user;
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).toList();

        try{
            user = userRepository.findUserByUniqueId(claims.getSubject());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        UserPrincipal principal = UserPrincipal.create(user.get());

        // JwtAuthenticationFilter에서 입력받은 Access Token + 유저 정보로 Authentication 객체 생성 및 리턴
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // JWT Authentication Filter에서 검증된 Refresh Token에서 email 추출 -> Redis에 존재하는지 확인 -> ATK Reissue
    // Exception 종류 변경 필요
    public String reissueAccessToken(Authentication authentication) throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String refToken = redisRepository.getValues(userPrincipal.getEmail());

        if(Objects.isNull(refToken)) throw new Exception("인증 정보가 만료되었습니다.");

        return createAcessToken(authentication);
    }

    public Boolean isValidToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalStateException e) {
            log.info("Wrong JWT Token");
        }
        return false;
    }

    // Access Token 만료 -> Token 갱신에 사용할 정보를 얻기 위해 Claim 값(JWT Token = Access Token을 복호화한 값)을 구함
    private Claims parseClaims(String accessToken) {
        try{
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}