package com.example.travel.route.repository;

import com.example.travel.route.entity.RouteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RouteImageRepository extends JpaRepository<RouteImage, Long> {
    @Query("insert into routeimage(route_id, img_id, img_url) values(#{route_id}, #{img_id}, #{img_url} )")
    void saveRouteImage(RouteImage routeImage);
}
