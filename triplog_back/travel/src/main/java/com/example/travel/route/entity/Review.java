package com.example.travel.route.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private RoutePlace place;

    @Column(name = "review_text")
    private String reviewText;

    @Builder
    public Review(Long id, RoutePlace place, String reviewText) {
        this.id = id;
        this.place = place;
        this.reviewText = reviewText;
    }

    @Builder
    public Review(Long id, RoutePlace place) {
        this.id = id;
        this.place = place;
    }

    @Builder
    public Review(String reviewText) {
        this.reviewText = reviewText;
    }
}