package com.bookinator.api.dao;

import com.bookinator.api.model.Wishlist;
import com.bookinator.api.model.dto.BookFilterResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/6/2018.
 */
@Mapper
public interface WishlistDAO {
    /**
     * Add to wishlist
     * @param request request
     */
    void addToWishlist(Wishlist request);

    /**
     * Remove from wishlist
     * @param request request
     */
    void removeFromWishlist(Wishlist request);

    /**
     * Get books from user wishlist
     * @param id user id
     * @return books in wishlist
     */
    List<BookFilterResponse> getUserWishlist(@Param("id") int id);

    /**
     * Count books in user wishlist (no idea why this is even needed...)
     * @param wishlist
     * @return
     */
    int count(Wishlist wishlist);
}
