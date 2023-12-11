package com.example.travel.auth.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes { // OAuth2User의 return 값은 Map 형태이므로, DB에 저장할 수 있는 형태로 변환해야 함

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String uniqueId;
    private final String email;
    private final String nickname;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String uniqueId, String email, String nickname) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.uniqueId = uniqueId;
        this.email = email;
        this.nickname = nickname;
    }

    public static OAuthAttributes of(String provider, String nameAttributeName, Map<String, Object> attributes) {
        if("kakao".equals(provider)) {
            return ofKakao(nameAttributeName, attributes);
        }

        return null; // 예외처리 필요
    }

    private static OAuthAttributes ofKakao(String nameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .uniqueId(attributes.get("id").toString())
                .email((String) kakaoAccount.get("email"))
                .nickname((String) profile.get("nickname"))
                .nameAttributeKey(nameAttributeName)
                .attributes(attributes)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .uniqueId(uniqueId)
                .email(email)
                .nickname(nickname)
                .roleType(RoleType.USER)
                .providerType(ProviderType.KAKAO)
                .build();
    }
}