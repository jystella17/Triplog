package com.example.travel.route.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "img_key")
    private String imgKey;

    @Column(name = "img_url")
    private String imgUrl;

    @Builder
    public Image(String imgName, String imgKey, String imgUrl) {
        this.imgName = imgName;
        this.imgKey = imgKey;
        this.imgUrl = imgUrl;
    }

    @Builder
    public Image(Long id, String imgName) {
        this.id = id;
        this.imgName = imgName;
    }
}