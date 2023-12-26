package com.example.travel.route.entity;

import com.example.travel.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "key_id", referencedColumnName = "id")
    private KeywordList keyword;

    @Column(name = "route_name")
    private String routeName;

    @Column(name = "route_day")
    private LocalDate routeDay;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "img_id", referencedColumnName = "id")
    private Image thumbnail;

    @Builder
    public Route(User owner, City city, KeywordList keyword, String routeName, LocalDate routeDay, Image thumbnail) {
        this.owner = owner;
        this.city = city;
        this.keyword = keyword;
        this.routeName = routeName;
        this.routeDay = routeDay;
        this.thumbnail = thumbnail;
    }

    @Builder
    public Route(Long id, User owner, City city, KeywordList keyword, String routeName, LocalDate routeDay, Image thumbnail) {
        this.id = id;
        this.owner = owner;
        this.city = city;
        this.keyword = keyword;
        this.routeName = routeName;
        this.routeDay = routeDay;
        this.thumbnail = thumbnail;
    }
}