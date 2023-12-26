package com.example.travel.route.service;

import com.example.travel.route.entity.City;
import com.example.travel.route.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CityService {

    private CityRepository cdao;
    public CityService(CityRepository cdao) {
        this.cdao = cdao;
    }

    public int addCities(City city){
        return cdao.addCities(city);
    }

    public City findCitiesById(Long city_id){
        return cdao.findCitiesById(city_id);
    }

    public List<City> findAllCities() throws SQLException {
        return cdao.findAllCities();
    }

    public int delete(Long city_id){
        return cdao.delete(city_id);
    }

    public int modify(City city){
        return  cdao.modify(city);
    }
}