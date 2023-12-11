package com.example.travel.auth.service;

import com.example.travel.auth.dto.request.RegisterDto;
import com.example.travel.auth.dto.response.UserRespDto;
import com.example.travel.auth.entity.ProviderType;
import com.example.travel.auth.entity.RoleType;
import com.example.travel.auth.entity.User;
import com.example.travel.auth.entity.UserPrincipal;
import com.example.travel.auth.provider.JwtTokenProvider;
import com.example.travel.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpServletResponse response;

    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider,
                       BCryptPasswordEncoder passwordEncoder, HttpServletResponse response) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
    }

    @Transactional
    public UserRespDto login(String email, String pw) throws Exception {
        User user = userRepository.login(email).orElseThrow();

        if(!passwordEncoder.matches(pw, user.getPw())) {
            throw new SQLException("Your email or password does not match");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication =
                new TestingAuthenticationToken(userPrincipal, user.getPw(), String.valueOf(RoleType.USER));

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        tokenProvider.createAcessToken(authentication);
        tokenProvider.createRefreshToken(authentication, response);

        return UserRespDto.builder().uid(user.getUid()).email(user.getEmail())
                .nickname(user.getNickname()).travelCount(user.getTravelCount()).level(user.getLevel()).build();
    }

    @Transactional
    public void register(String email, String pw, String nickname) throws SQLException {
        if(userRepository.findUserByEmail(email).isEmpty()) {
            pw = passwordEncoder.encode(pw);
            String unique = passwordEncoder.encode(pw).substring(7, 20);
            RegisterDto registerInfo = new RegisterDto(unique, pw, email, nickname, RoleType.USER, ProviderType.NO);

            userRepository.save(registerInfo);
        }
        else {
            throw new SQLException("이미 가입된 유저입니다.");
        }
    }
    @Transactional
    public void modifyNickname(Long uid, String nickname) {
        userRepository.updateUserByNickname(nickname);
    }

    @Transactional
    public String newRefreshToken(Authentication authentication) throws Exception {
        String rtk;

        try {
            rtk = tokenProvider.reissueAccessToken(authentication);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new Exception("Refresh Token Update 실패");
        }

        return rtk;
    }

    public void changePassword(Long uid, String curPw, String newPw) throws SQLException {
        String originalPw = String.valueOf(userRepository.findUserByUid(uid));

        if(!passwordEncoder.matches(curPw, originalPw)) {
            throw new SQLException("Your email or password does not match");
        }

        newPw = passwordEncoder.encode(newPw);
        userRepository.updateUserByPw(newPw);
    }

    public void logout() {
        SecurityContext context = SecurityContextHolder.getContext();
        // 인증 객체 삭제
        context.setAuthentication(null);
        // Security Context 삭제
        SecurityContextHolder.clearContext();
    }
}