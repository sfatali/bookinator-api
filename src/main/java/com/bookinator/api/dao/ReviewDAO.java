package com.bookinator.api.dao;

import com.bookinator.api.model.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/6/2018.
 */
@Mapper
public interface ReviewDAO {
    void create(Review review);
    void update(Review review);
    Review getReviewById(@Param("id") int id);
    List<com.bookinator.api.model.dto.Review> getUserReviews(@Param("id") int id);
}
