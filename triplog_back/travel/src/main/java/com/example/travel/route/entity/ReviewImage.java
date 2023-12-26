package com.example.travel.route.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "review_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn()
    private Review review;

    @OneToOne()
    @JoinColumn()
    private Image image;

    @Builder
    public ReviewImage(Review review, Image image) {
        this.review = review;
        this.image = image;
    }

    @Builder
    public ReviewImage (Long id, Review review, Image image) {
        this.id = id;
        this.review = review;
        this.image = image;
    }
}