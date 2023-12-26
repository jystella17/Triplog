package com.example.travel.route.controller;

import com.example.travel.route.entity.City;
import com.example.travel.route.service.CitiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/cities")
public class CityController {

    private final CitiesService service;

    public CityController(CitiesService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> findAllCities() {
        try{
            List<City> cityList = service.findAllCities();

            if(cityList != null && !cityList.isEmpty()) {
                return new ResponseEntity<>(cityList, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }

        } catch (SQLException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}