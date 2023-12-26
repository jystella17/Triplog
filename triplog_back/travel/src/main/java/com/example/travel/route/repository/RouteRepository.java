package com.example.travel.route.repository;

import com.example.travel.auth.entity.User;
import com.example.travel.route.entity.KeywordList;
import com.example.travel.route.dto.response.RouteResponseDto;
import com.example.travel.route.entity.Route;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByKeywordOrderById(KeywordList keyword);

    @Query(value = "select * from Route where route_name like '%:routeName%'", nativeQuery = true)
    List<Route> findByRouteName(String routeName);

    List<Route> findAllByOwner(User user);

    List<Route> findBySpot(String spotName);
}