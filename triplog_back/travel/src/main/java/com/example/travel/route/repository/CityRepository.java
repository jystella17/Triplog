package com.example.travel.route.repository;

import com.example.travel.route.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

}