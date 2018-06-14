package com.bookinator.api;

import com.bookinator.api.dao.BookDAO;
import com.bookinator.api.model.Book;
import com.bookinator.api.model.dto.BookFilterRequest;
import com.bookinator.api.model.dto.BookFilterResponse;
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
 * Created by Sabina on 5/1/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class BookDAOTest {
    @Autowired
    private BookDAO bookDAO;
    Book testBook;

    @Before
    public void init() {
        testBook = new Book();
        testBook.setOwnerId(1);
        testBook.setHoldingTypeId(1);
        testBook.setStatusId(1);
        testBook.setName("BookFilterResponse name");
        testBook.setAuthors("author;");
        testBook.setFieldId(1);
        testBook.setYearPublished(2001);
        testBook.setTopics("topic;");
        testBook.setDescription("Desc");
    }

    @Test
    public void testBookCreate() {
        bookDAO.create(testBook);
        Book bookFromDb = bookDAO.getBookById(testBook.getId());
        Assert.assertNotNull(bookFromDb);
        Assert.assertEquals(testBook.getAuthors(), bookFromDb.getAuthors());
        Assert.assertEquals(testBook.getDescription(), bookFromDb.getDescription());
        Assert.assertEquals(testBook.getName(), bookFromDb.getName());
        Assert.assertEquals(testBook.getOwnerId(), bookFromDb.getOwnerId());
        Assert.assertEquals(testBook.getHoldingTypeId(), bookFromDb.getHoldingTypeId());
        Assert.assertEquals(testBook.getStatusId(), bookFromDb.getStatusId());
        Assert.assertEquals(testBook.getFieldId(), bookFromDb.getFieldId());
        Assert.assertEquals(testBook.getYearPublished(), bookFromDb.getYearPublished());
        Assert.assertEquals(testBook.getTopics(), bookFromDb.getTopics());
    }

    @Test
    public void testBookUpdate() {
        bookDAO.create(testBook);
        testBook.setHoldingTypeId(2);
        testBook.setStatusId(2);
        testBook.setName("New book name");
        testBook.setAuthors("new author;");
        testBook.setFieldId(2);
        testBook.setYearPublished(2002);
        testBook.setTopics("new topic;");
        testBook.setDescription("new desc");
        bookDAO.update(testBook);
        Book bookFromDb = bookDAO.getBookById(testBook.getId());
        Assert.assertNotNull(bookFromDb);
        Assert.assertEquals(testBook.getAuthors(), bookFromDb.getAuthors());
        Assert.assertEquals(testBook.getDescription(), bookFromDb.getDescription());
        Assert.assertEquals(testBook.getName(), bookFromDb.getName());
        Assert.assertEquals(testBook.getHoldingTypeId(), bookFromDb.getHoldingTypeId());
        Assert.assertEquals(testBook.getStatusId(), bookFromDb.getStatusId());
        Assert.assertEquals(testBook.getFieldId(), bookFromDb.getFieldId());
        Assert.assertEquals(testBook.getYearPublished(), bookFromDb.getYearPublished());
        Assert.assertEquals(testBook.getTopics(), bookFromDb.getTopics());
    }

    @Test
    public void testBookDelete() {
        bookDAO.create(testBook);
        bookDAO.delete(testBook.getId());
        Book bookFromDb = bookDAO.getBookById(testBook.getId());
        Assert.assertNull(bookFromDb);
    }

    @Test
    public void testBooksFilter() {
        // when no fields for filter are assigned, just all bookFilterResponses are returned
        BookFilterRequest filterRequest = new BookFilterRequest();
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(8, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByName() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setName("The Lord of the Rings");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByAuthor() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setAuthor("Tolkien");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByAllFields() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setName("The Lord of the Rings");
        filterRequest.setAuthor("Tolkien");
        filterRequest.setFieldId(1);
        filterRequest.setCityId(1);
        filterRequest.setTopic("fantasy");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByYear() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setYearPublished(1988);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(1, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByField() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setFieldId(5);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(4, bookFilterResponses.size());
    }

    @Test
    public void testBooksFilterByCity() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setCityId(1);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(8, bookFilterResponses.size());
    }

    @Test
    public void testGetUserBooks() {
        List<BookFilterResponse> bookFilterResponses = bookDAO.getUserBooks(4);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    @Test
    public void testGetBookDto() {
        com.bookinator.api.model.dto.Book book = bookDAO.getBookDTO(1);
        Assert.assertNotNull(book);
    }
}
