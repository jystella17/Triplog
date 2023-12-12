package com.example.travel.route.repository;

import com.example.travel.route.entity.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;

public interface ImageRepository extends JpaRepository<Image, Long> {

}