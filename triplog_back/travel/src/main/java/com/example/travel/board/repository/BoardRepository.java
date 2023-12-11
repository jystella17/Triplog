package com.example.travel.board.repository;

import com.example.travel.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
import java.sql.SQLException;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findBy(Long id) throws SQLException;

    @Query(value = "update Board set hit = hit + 1 where id = :id")
    void updateBoardByHit(Long id) throws SQLException;

    @Query(value = "update Board set subject = :subject, content = :content where id = :id")
    void updateArticle(String subject, String content, Long id) throws SQLException;
}