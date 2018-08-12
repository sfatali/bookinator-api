package com.bookinator.api.dao;

import com.bookinator.api.model.Wish;
import com.bookinator.api.model.dto.ExploreResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sabina on 5/6/2018.
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class WishlistDAOTest {
    private static final Logger logger =
            Logger.getLogger(WishlistDAOTest.class.getSimpleName());
    @Autowired
    private WishlistDAO wishlistDAO;

    /**
     * Testing adding book to wishlist
     */
    @Test
    public void testAddToWishList() {
        logger.info("Testing adding book to wishlist");
        Wish wish = new Wish();
        wish.setBookId(3);
        wish.setUserId(3);
        wishlistDAO.addToWishlist(wish);

        List<ExploreResponse> bookFilterResponses = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(bookFilterResponses);
        Assert.assertEquals(4, bookFilterResponses.size());
    }

    /**
     * Testing removing book from wishlist
     */
    @Test
    public void testRemoveFromWishlist() {
        logger.info("Testing removing book from wishlist");
        Wish wish = new Wish();
        wish.setBookId(1);
        wish.setUserId(3);
        wishlistDAO.removeFromWishlist(wish);

        List<ExploreResponse> bookFilterResponses = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(bookFilterResponses);
        Assert.assertEquals(2, bookFilterResponses.size());
    }
}
