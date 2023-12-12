package com.example.travel.route.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "route_place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long spot_id;

    private Long route_id;

    private String rp_name;

    private int priority;

    private LocalDateTime visit_time;


    @Builder
    public RoutePlace(Long spot_id, Long route_id, String rp_name, int priority, LocalDateTime visit_time) {
        this.spot_id = spot_id;
        this.route_id = route_id;
        this.rp_name = rp_name;
        this.priority = priority;
        this.visit_time = visit_time;
    }


    @Builder
    public RoutePlace(Long id, Long spot_id, Long route_id, String rp_name, int priority, LocalDateTime visit_time) {
        this.id = id;
        this.spot_id = spot_id;
        this.route_id = route_id;
        this.rp_name = rp_name;
        this.priority = priority;
        this.visit_time = visit_time;
    }

    public void changePriority(int priority) {
        this.priority = priority;
    }
}