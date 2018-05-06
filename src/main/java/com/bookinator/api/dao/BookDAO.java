package com.bookinator.api.dao;

import com.bookinator.api.model.Book;
import com.bookinator.api.model.dto.BookFilterRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/1/2018.
 */
@Mapper
public interface BookDAO {
    void create(Book book);
    void update(Book book);
    void delete(@Param("id") int id);
    Book getBookById(@Param("id") int id);
    List<com.bookinator.api.model.dto.Book> filterBooks(BookFilterRequest filter);
    List<com.bookinator.api.model.dto.Book> getUserBooks(@Param("id") int id);
}