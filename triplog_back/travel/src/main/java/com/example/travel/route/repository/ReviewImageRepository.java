package com.example.travel.route.repository;

import com.example.travel.route.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select rv_id, review_id, img_id, img_url from reviewimage where review_id=#{review_id}")
    List<ReviewImage> findReviewImages(Long review_id);

    @Query("insert into reviewimage(review_id, img_id, img_url) values(#{review_id}, #{img_id}, #{img_url})")
    void saveReviewImage(Long review_id, Long img_id, String img_url);
}
