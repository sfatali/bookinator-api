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
    void addToWishlist(Wishlist request);
    void removeFromWishlist(Wishlist request);
    List<BookFilterResponse> getUserWishlist(@Param("id") int id);
    int count(Wishlist wishlist);
}
