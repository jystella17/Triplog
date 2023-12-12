package com.example.travel.route.repository;

import com.example.travel.route.entity.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CitiesRepository extends JpaRepository<Cities, Long> {

}