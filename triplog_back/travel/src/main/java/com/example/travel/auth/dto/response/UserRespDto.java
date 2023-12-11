package com.example.travel.auth.dto.response;

import com.example.travel.auth.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRespDto {

    private final Long uid;
    private final String email;
    private final String nickname;
    private final int travelCount;
    private final int level;

    public UserRespDto(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.travelCount = user.getTravelCount();
        this.level = user.getLevel();
    }

    @Builder
    public UserRespDto(Long uid, String email, String nickname, int travelCount, int level) {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.travelCount = travelCount;
        this.level = level;
    }
}