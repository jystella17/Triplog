package com.example.travel.auth.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "pw")
    private String pw;

    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "role_type")
    private RoleType roleType;

    @Column(name = "provider_type")
    private ProviderType providerType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "travel_count")
    private Integer travelCount;

    @Column(name = "level")
    private Integer level;

    @Builder // 소셜 로그인으로 가입하는 경우
    public User(String uniqueId, String email, String nickname, RoleType roleType, ProviderType providerType,
                LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.uniqueId = uniqueId;
        this.pw = "";
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.travelCount = 0;
        this.level = 1;
    }

    @Builder // 자체 로그인으로 가입
    public User(String uniqueId, String pw, String email, String nickname, RoleType roleType) {
        this.uniqueId = uniqueId; // 자체 로그인으로 가입 시 초기에는 Emtpy String => 이메일 인증 후 UUID 발급
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = ProviderType.NO;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.travelCount = 0;
        this.level = 1;
    }

    @Builder // saveRefreshToken의 Target User
    public User (Long uid, String unique_id, String pw, String email, String nickname,
                 RoleType roleType, ProviderType providerType, int travelCount, int level) {
        this.uid = uid;
        this.uniqueId = unique_id;
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.travelCount = travelCount;
        this.level = level;
    }

    @Builder
    public User(String nickname, LocalDateTime modifiedAt) { // 닉네임을 변경하는 경우
        this.nickname = nickname;
        this.modifiedAt = modifiedAt;
    }

    @Builder
    public User(Integer travelCount, Integer level) { // 여행을 완료하여 여행 횟수&레벨을 변경하는 경우
        this.travelCount = travelCount;
        this.level = level;
    }

    @Builder
    public User(Long uid, String email, String nickname) { // 자체 로그인 유저가 이미 가입되어 있는지 확인
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
    }

    public void changeRole(RoleType roleType) { // 유저 권한 변경
        this.roleType = roleType;
    }
}