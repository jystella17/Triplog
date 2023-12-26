package com.example.travel.route.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "city")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Builder
    public City(Long id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    @Builder
    public City(String cityName) {
        this.cityName = cityName;
    }
}