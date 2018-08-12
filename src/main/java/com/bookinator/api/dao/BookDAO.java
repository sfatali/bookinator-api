package com.bookinator.api.dao;

import com.bookinator.api.model.Book;
import com.bookinator.api.model.dto.ExploreRequest;
import com.bookinator.api.model.dto.ExploreResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Sabina on 5/1/2018.
 */
@Mapper
public interface BookDAO {
    /**
     * Creating a new book
     * @param book book id
     */
    void create(Book book);

    /**
     * Updating a book
     * @param book book id
     */
    void update(Book book);

    /**
     * Deleting a book
     * @param id book id
     */
    void delete(@Param("id") int id);

    /**
     * Get book by its id - in a way that is stored in database
     * @param id book id
     * @return Book
     */
    Book getBookById(@Param("id") int id);

    /**
     * Returns books based on search criteria
     * @param filter parameters for search
     * @return list of books
     */
    List<ExploreResponse> filterBooks(ExploreRequest filter);

    /**
     * Returns user's books
     * @param id user id
     * @return list of books
     */
    List<com.bookinator.api.model.dto.Book> getUserBooks(@Param("id") int id);

    /**
     * @param id book id
     * @return book DTO
     */
    com.bookinator.api.model.dto.Book getBookDTO(@Param("id") int id);

    /**
     * Ger id of book owner
     * @param id book id
     * @return user id
     */
    int getBookOwnerId(@Param("id") int id);
}
