package com.example.travel.board.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.Constraint;

import com.example.travel.auth.entity.User;

@Getter
@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private User writer;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "hit")
    private int hit;

    @Column(name = "register_time")
    private String registerTime;

    @Builder
    public Board(User writer, String subject, String content) {
        this.writer = writer;
        this.subject = subject;
        this.content = content;
    }

    @Builder
    public Board(User writer, String subject, String content, int hit, String registerTime) {
        this.writer = writer;
        this.subject = subject;
        this.content = content;
        this.hit = hit;
        this.registerTime = registerTime;
    }

    @Builder
    public Board(Long id, User writer, String subject, String content, int hit, String registerTime) {
        this.id = id;
        this.writer = writer;
        this.subject = subject;
        this.content = content;
        this.hit = hit;
        this.registerTime = registerTime;
    }
}