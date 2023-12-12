package com.example.travel.route.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "keyword_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "keyword")
    private String keyword;

    @Builder
    public KeywordList(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
}