package com.example.travel.route.dto.response;

import lombok.Getter;
import lombok.Builder;

import java.time.LocalDate;

@Getter
public class RouteResponseDto {

    private final Long routeId;

    private final String cityName;

    private final String keyword;

    private final String routeName;

    private final LocalDate routeDay;

    private final String imgUrl;

    @Builder
    public RouteResponseDto(Long routeId, String cityName, String keyword, String routeName, LocalDate routeDay, String imgUrl) {
        this.routeId = routeId;
        this.cityName = cityName;
        this.keyword = keyword;
        this.routeName = routeName;
        this.routeDay = routeDay;
        this.imgUrl = imgUrl;
    }
}