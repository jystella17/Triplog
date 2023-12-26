package com.example.travel.route.service;

import com.example.travel.route.dto.response.RouteResponseDto;
import com.example.travel.route.entity.City;
import com.example.travel.route.entity.KeywordList;
import com.example.travel.route.entity.Route;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RouteServiceTest {

    @Autowired
    private RouteService routeService;
    @Autowired
    private RoutePlaceService placeService;
    @Autowired
    private SpotInfoService spotInfoService;
    @Autowired
    private CityService cityService;
    @Autowired
    private KeywordListService keywordListService;
    @Test
    void selectAll() {
        List<RouteResponseDto> routeList = routeService.findAllRoute();
        System.out.println(routeList);
    }

    @Test
    void selectOne() {
        Long rout_id = 1L;
        Route route = routeService.findRouteById(rout_id);
        System.out.println(route);
    }

    @Test
    void regist() {
        // 1. Cities, User, Keyword 조회
        // 2. Route 생성
        // 3. SpotInfo 조회
        // 4. RoutePlace 생성
        Long city_id = 1L;
        City city = cityService.findCitiesById(city_id);
        Long uid = 1L;
        KeywordList key1 = keywordListService.findKeywordListById(1L);
        KeywordList key2 = keywordListService.findKeywordListById(2L);

//        Route newRoute = Route.builder().uid(uid).city_id(city.getCity_id()).keyword1(key1.getKey_id()).keyword2(key2.getKey_id()).route_name("재밌는 여행").route_day(new Date()).build();
//        int ans = routeService.addRoute(newRoute);
//        // selectKey
//        System.out.println(ans);
    }

    @Test
    void delete() {
        Long route_id = 1L;
        int ans = routeService.delete(route_id);
        System.out.println(ans);
    }

    @Test
    void modify() {

    }
}