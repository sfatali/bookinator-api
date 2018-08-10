package com.bookinator.api.dao;

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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sabina on 5/1/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class BookDAOTest {
    private static final Logger logger =
            Logger.getLogger(BookDAOTest.class.getSimpleName());
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private UserDAO userDAO;
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

    /**
     * Testing book creation
     */
    @Test
    public void testBookCreate() {
        logger.info("Testing book creation");
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

    /**
     * Testing book creation with non-existing owner id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testBookCreateInvalidOwner() {
        logger.info("Testing book creation with non-existing owner id - violation foreign key policy");
        testBook.setOwnerId(10000);
        bookDAO.create(testBook);
    }

    /**
     * Testing book creation with non-existing holding type id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testBookCreateInvalidHoldingType() {
        logger.info("Testing book creation with non-existing holding type id - violation foreign key policy");
        testBook.setHoldingTypeId(10000);
        bookDAO.create(testBook);
    }

    /**
     * Testing book creation with non-existing book status id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testBookCreateInvalidBookStatus() {
        logger.info("Testing book creation with non-existing book status id - violation foreign key policy");
        testBook.setStatusId(10000);
        bookDAO.create(testBook);
    }

    /**
     * Testing book update
     */
    @Test
    public void testBookUpdate() {
        logger.info("Testing book update");
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

    /**
     * Testing book update with non-existing owner id
     */
    @Test
    public void testBookUpdateInvalidOwner() {
        logger.info("Testing book update with non-existing owner id - nothing changes for the book");
        bookDAO.create(testBook);
        testBook.setOwnerId(10000);
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

    /**
     * Testing book update with non-existing holding type id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testBookUpdateInvalidHoldingType() {
        logger.info("Testing book update with non-existing holding type id - violation foreign key policy");
        bookDAO.create(testBook);
        testBook.setHoldingTypeId(10000);
        bookDAO.update(testBook);
    }
    /**
     * Testing book update with non-existing book status id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testBookUpdateInvalidBookStatus() {
        logger.info("Testing book update with non-existing book status id - violation foreign key policy");
        bookDAO.create(testBook);
        testBook.setStatusId(10000);
        bookDAO.update(testBook);
    }

    /**
     * Testing book deletion
     */
    @Test
    public void testBookDelete() {
        logger.info("Testing book deletion");
        bookDAO.create(testBook);
        bookDAO.delete(testBook.getId());
        Book bookFromDb = bookDAO.getBookById(testBook.getId());
        Assert.assertNull(bookFromDb);
    }

    /**
     * Testing book deletion by cascade, when owner is deleted
     */
    @Test
    public void testBookDeleteInvalidBook() {
        logger.info("Testing book deletion by cascade, when owner is deleted");
        bookDAO.create(testBook);
        userDAO.delete(testBook.getOwnerId());
        Book bookFromDb = bookDAO.getBookById(testBook.getId());
        Assert.assertNull(bookFromDb);
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilter() {
        logger.info("Testing book filter: case when no seacrh params specified");
        // when no fields for filter are assigned, just all bookFilterResponses are returned
        BookFilterRequest filterRequest = new BookFilterRequest();
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(8, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByName() {
        logger.info("Testing book filter: case when name is specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setName("The Lord of the Rings");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByAuthor() {
        logger.info("Testing book filter: case when author is specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setAuthor("Tolkien");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByAllFields() {
        logger.info("Testing book filter: case when all params are specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setName("The Lord of the Rings");
        filterRequest.setAuthor("Tolkien");
        filterRequest.setFieldId(1);
        filterRequest.setCityId(1);
        filterRequest.setTopic("fantasy");
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByYear() {
        logger.info("Testing book filter: case when publication year is specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setYearPublished(1988);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(1, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByField() {
        logger.info("Testing book filter: case when book's field is specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setFieldId(5);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(4, bookFilterResponses.size());
    }

    /**
     * Testing book filter
     */
    @Test
    public void testBooksFilterByCity() {
        logger.info("Testing book filter: case when city of book owner is specified");
        BookFilterRequest filterRequest = new BookFilterRequest();
        filterRequest.setCityId(1);
        List<BookFilterResponse> bookFilterResponses = bookDAO.filterBooks(filterRequest);
        Assert.assertEquals(8, bookFilterResponses.size());
    }

    /**
     * Testing getting user's books
     */
    @Test
    public void testGetUserBooks() {
        logger.info("Testing getting user's books");
        List<com.bookinator.api.model.dto.Book> bookFilterResponses = bookDAO.getUserBooks(4);
        Assert.assertEquals(3, bookFilterResponses.size());
    }

    /**
     * Testing getting book by its ID
     */
    @Test
    public void testGetBookDto() {
        logger.info("Testing getting book by its ID");
        com.bookinator.api.model.dto.Book book = bookDAO.getBookDTO(1);
        Assert.assertNotNull(book);
    }
}
