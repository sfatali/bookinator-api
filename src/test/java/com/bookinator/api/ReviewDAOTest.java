package com.bookinator.api;

import com.bookinator.api.dao.ReviewDAO;
import com.bookinator.api.model.Review;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * Created by Sabina on 5/6/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class ReviewDAOTest {
    @Autowired
    private ReviewDAO reviewDAO;
    Review testReview;

    @Before
    public void init() {
        testReview = new Review();
        testReview.setHoldingRequestId(2);
        testReview.setReviewerId(3);
        testReview.setRevieweeId(4);
        testReview.setReviewText("text");
        testReview.setScore(4);
    }

    @Test
    public void testAddReview() {
        reviewDAO.create(testReview);
        Review reviewFromDb = reviewDAO.getReviewById(testReview.getId());
        Assert.assertNotNull(reviewFromDb);
        Assert.assertEquals(testReview.getReviewerId(), reviewFromDb.getReviewerId());
        Assert.assertEquals(testReview.getRevieweeId(), reviewFromDb.getRevieweeId());
        Assert.assertEquals(testReview.getScore(), reviewFromDb.getScore());
        Assert.assertEquals(testReview.getReviewText(), reviewFromDb.getReviewText());
        Assert.assertEquals(testReview.getHoldingRequestId(), reviewFromDb.getHoldingRequestId());
    }

    @Test
    public void testUpdateReview() {
        reviewDAO.create(testReview);
        testReview.setScore(2);
        testReview.setReviewText("new text");
        reviewDAO.update(testReview);
        Review reviewFromDb = reviewDAO.getReviewById(testReview.getId());
        Assert.assertNotNull(reviewFromDb);
        Assert.assertEquals(testReview.getReviewerId(), reviewFromDb.getReviewerId());
        Assert.assertEquals(testReview.getRevieweeId(), reviewFromDb.getRevieweeId());
        Assert.assertEquals(testReview.getScore(), reviewFromDb.getScore());
        Assert.assertEquals(testReview.getReviewText(), reviewFromDb.getReviewText());
        Assert.assertEquals(testReview.getHoldingRequestId(), reviewFromDb.getHoldingRequestId());
    }

    @Test
    public void testGetUserReviews() {
        List<com.bookinator.api.model.dto.Review> reviews = reviewDAO.getUserReviews(4);
        Assert.assertNotNull(reviews);
        Assert.assertEquals(1, reviews.size());
    }
}
