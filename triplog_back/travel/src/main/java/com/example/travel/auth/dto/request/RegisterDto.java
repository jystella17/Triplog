package com.example.travel.auth.dto.request;

import com.example.travel.auth.entity.ProviderType;
import com.example.travel.auth.entity.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class RegisterDto implements Serializable {

    private final String uniqueId;
    private final String pw;
    private final String email;
    private final String nickname;
    private final RoleType roleType;
    private final ProviderType providerType;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    public RegisterDto(String uniqueId, String pw, String email, String nickname,
                       RoleType roleType, ProviderType providerType) {
        this.uniqueId = uniqueId;
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}