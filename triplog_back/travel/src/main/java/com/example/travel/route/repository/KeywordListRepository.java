package com.example.travel.route.repository;

import com.example.travel.route.entity.KeywordList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordListRepository extends JpaRepository<KeywordList, Long> {

    List<Long> findKeywordListByName(List<String> keyNames);
}