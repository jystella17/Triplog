package com.example.travel.route.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "cities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Builder
    public Cities(Long id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }

    @Builder
    public Cities(String cityName) {
        this.cityName = cityName;
    }
}