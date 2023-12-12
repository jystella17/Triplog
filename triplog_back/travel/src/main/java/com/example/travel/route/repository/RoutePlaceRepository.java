package com.example.travel.route.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.travel.route.dto.request.PlaceModifyDto;
import com.example.travel.route.entity.RoutePlace;

import java.util.List;

public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {

    List<RoutePlace> findAllRoutePlace();
    RoutePlace findRoutePlaceById(Long place_id);
    List<RoutePlace> findRoutePlaceByRoute(Long route_id);
    int addRoutePlace(RoutePlace place);
    void deleteById(Long place_id);

    int modifyPriorityById(PlaceModifyDto placeModifyDto);
    int delete(Long place_id);
    int modify(RoutePlace place);
}