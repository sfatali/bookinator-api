package com.bookinator.api.dao;

import com.bookinator.api.dao.WishlistDAO;
import com.bookinator.api.model.Wishlist;
import com.bookinator.api.model.dto.BookFilterResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Sabina on 5/6/2018.
 */
@RunWith(SpringRunner.class)
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
        Wishlist wishlistRequest = new Wishlist();
        wishlistRequest.setBookId(3);
        wishlistRequest.setUserId(3);
        wishlistDAO.addToWishlist(wishlistRequest);

        List<BookFilterResponse> bookFilterResponses = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(bookFilterResponses);
        Assert.assertEquals(4, bookFilterResponses.size());
    }

    /**
     * Testing removing book from wishlist
     */
    @Test
    public void testRemoveFromWishlist() {
        logger.info("Testing removing book from wishlist");
        Wishlist wishlistRequest = new Wishlist();
        wishlistRequest.setBookId(1);
        wishlistRequest.setUserId(3);
        wishlistDAO.removeFromWishlist(wishlistRequest);

        List<BookFilterResponse> bookFilterResponses = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(bookFilterResponses);
        Assert.assertEquals(2, bookFilterResponses.size());
    }
}
