package com.example.travel.route.repository;

import com.example.travel.route.dto.response.RouteResponseDto;
import com.example.travel.route.entity.Route;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouteRepository {

    List<RouteResponseDto> findAllRoute();
    RouteResponseDto findRouteById(Long route_id);
    List<RouteResponseDto> findRouteByName(String route_name);
    List<RouteResponseDto> findRouteByKeyName(String keyword);
    List<RouteResponseDto> findRouteByKeyNameTop3(String keyword);
    List<RouteResponseDto> findRouteByCityName(String city_name);
    List<RouteResponseDto> findRouteByKeyword(Long key_id);
    List<RouteResponseDto> findRouteByUser(Long uid);
    List<RouteResponseDto> findAllRouteByUser(Long uid);
    List<RouteResponseDto> findRouteBySpot(String spot_name);
    Long addRoute(Route route);
    int delete(Long route_id);
    int modify(Route route);
}