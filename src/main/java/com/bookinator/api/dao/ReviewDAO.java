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
    /**
     * Creating review
     * @param review review
     */
    void create(Review review);

    /**
     * Updating review
     * @param review review
     */
    void update(Review review);

    /**
     * Get review by id - in a way stored in db
     * @param id review id
     * @return review
     */
    Review getReviewById(@Param("id") int id);

    /**
     * Get user's reviews - in a readable way
     * @param id user id
     * @return reviews
     */
    List<com.bookinator.api.model.dto.Review> getUserReviews(@Param("id") int id);
}
