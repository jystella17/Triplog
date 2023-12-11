package com.example.travel.auth.service;

import com.example.travel.auth.entity.*;
import com.example.travel.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // Naver, Kakao 중 어느 계정인지
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 각 계정의 고유 id (소셜 서비스마다 고유 id를 저장하는 필드의 이름이 다르므로, 먼저 해당 필드명을 찾고 데이터를 가져와야 함)
        String nameAttributeName = "";
        if(provider.equals("kakao")) {
            nameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUserNameAttributeName();
        }

        OAuthAttributes attributes = OAuthAttributes.of(provider, nameAttributeName, oAuth2User.getAttributes());

        if(attributes == null) {
            // 사용자 정보를 가져올 수 없는 경우의 예외 처리 구현
            throw new Exception();
        }

        // DB에서 찾을 수 있는 이미 가입된 유저인지
        Optional<User> isAlready = userRepository.findUserByUniqueId(attributes.getUniqueId());
        if(isAlready.isEmpty()) {
            return UserPrincipal.create(isAlready.get(), oAuth2User.getAttributes());
        }

        isAlready = Optional.of(createUser(attributes, provider));
        return UserPrincipal.create(isAlready.get(), oAuth2User.getAttributes());
    }

    private User createUser(OAuthAttributes attributes, String provider) throws SQLException {
        ProviderType pt = ProviderType.NO;
        if(provider.equals("kakao")) {
            pt = ProviderType.KAKAO;
        }

        User user = new User(attributes.getUniqueId(), attributes.getEmail(), attributes.getNickname(),
                RoleType.USER, pt, LocalDateTime.now(), LocalDateTime.now());

        userRepository.save(user);
        return user;
    }
}