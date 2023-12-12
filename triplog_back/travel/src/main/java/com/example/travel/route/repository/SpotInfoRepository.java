package com.example.travel.route.repository;

import com.example.travel.route.entity.SpotInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.List;

public interface SpotInfoRepository extends JpaRepository<SpotInfo, Long> {

    List<SpotInfo> findAllSpotInfo();
    SpotInfo findSpotInfoById(Long spot_id);
    List<SpotInfo> findSpotInfoByName(String spotName) throws SQLException;
    int addSpotInfo(List<SpotInfo> spotInfoList);
    int addOneSpotInfo(SpotInfo spotInfo) throws SQLException;
    int delete(Long spot_id);
    int modify(SpotInfo spot);
}