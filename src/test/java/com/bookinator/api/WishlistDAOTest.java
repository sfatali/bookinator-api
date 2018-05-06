package com.bookinator.api;

import com.bookinator.api.dao.WishlistDAO;
import com.bookinator.api.model.Wishlist;
import com.bookinator.api.model.dto.Book;
import org.junit.Assert;
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
public class WishlistDAOTest {
    @Autowired
    private WishlistDAO wishlistDAO;

    @Test
    public void testAddToWishList() {
        Wishlist wishlistRequest = new Wishlist();
        wishlistRequest.setBookId(3);
        wishlistRequest.setUserId(3);
        wishlistDAO.addToWishlist(wishlistRequest);

        List<Book> books = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(books);
        Assert.assertEquals(4, books.size());
    }

    @Test
    public void testRemoveFromWishlist() {
        Wishlist wishlistRequest = new Wishlist();
        wishlistRequest.setBookId(1);
        wishlistRequest.setUserId(3);
        wishlistDAO.removeFromWishlist(wishlistRequest);

        List<Book> books = wishlistDAO.getUserWishlist(3);
        Assert.assertNotNull(books);
        Assert.assertEquals(2, books.size());
    }
}
