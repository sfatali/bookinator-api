package com.bookinator.api.controller.helpers;

import com.bookinator.api.model.dto.Review;
import com.bookinator.api.resources.ReviewResource;

import java.util.List;

/**
 * Created by Sabina on 8/12/2018.
 */
public class ReviewHelper {
    public static ReviewResource getReviewResource(Review review) {
        ReviewResource reviewResource = new ReviewResource();
        reviewResource.setAuthor(review.getAuthor());
        reviewResource.setDate(review.getDate());
        reviewResource.setReviewId(review.getId());
        reviewResource.setReviewText(review.getReviewText());
        reviewResource.setScore(review.getScore());
        return reviewResource;
    }
}
