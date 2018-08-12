package com.bookinator.api.dao;

import com.bookinator.api.model.HoldingRequest;
import com.bookinator.api.model.dto.BookRequest;
import com.bookinator.api.model.dto.UpdateBookRequestStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
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
public class BookHoldingsDAOTest extends RunListener {
    private static final Logger logger =
            Logger.getLogger(BookHoldingsDAOTest.class.getSimpleName());
    @Autowired
    private BookHoldingsDAO bookHoldingsDAO;
    private HoldingRequest testHoldingRequest;

    @Before
    public void init() {
        testHoldingRequest = new HoldingRequest();
        testHoldingRequest.setSenderId(3);
        testHoldingRequest.setReceiverId(1);
        testHoldingRequest.setBookId(6);
        testHoldingRequest.setParentBookId(7);
        testHoldingRequest.setStatusId(1);
        testHoldingRequest.setRequestMessage("look at this book");
    }

    /**
     * Testing creation of book's holding request
     */
    @Test
    public void testCreate() {
        logger.info("Testing creation of book's holding request");
        bookHoldingsDAO.create(testHoldingRequest);
        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
        Assert.assertNotNull(holdingRequestFromDb);
        Assert.assertEquals(testHoldingRequest.getSenderId(), holdingRequestFromDb.getSenderId());
        Assert.assertEquals(testHoldingRequest.getReceiverId(), holdingRequestFromDb.getReceiverId());
        Assert.assertEquals(testHoldingRequest.getBookId(), holdingRequestFromDb.getBookId());
        Assert.assertEquals(testHoldingRequest.getParentBookId(), holdingRequestFromDb.getParentBookId());
        Assert.assertEquals(testHoldingRequest.getStatusId(), holdingRequestFromDb.getStatusId());
        Assert.assertEquals(testHoldingRequest.getRequestMessage(), holdingRequestFromDb.getRequestMessage());
    }

    /**
     * Testing request creation with invalid sender
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateInvalidSender() {
        logger.info("Testing request creation with invalid sender");
        testHoldingRequest.setSenderId(1500);
        bookHoldingsDAO.create(testHoldingRequest);
        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
    }

    /**
     * Testing request creation with invalid receiver
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateInvalidReciver() {
        logger.info("Testing request creation with invalid receiver");
        testHoldingRequest.setReceiverId(1500);
        bookHoldingsDAO.create(testHoldingRequest);
        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
    }

    /**
     * Testing request creation with invalid book id
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testCreateInvalidBook() {
        logger.info("Testing request creation with invalid book id");
        testHoldingRequest.setBookId(1500);
        bookHoldingsDAO.create(testHoldingRequest);
        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
    }

    /**
     * Testing status update of book's holding request
     */
    @Test
    public void testStatusUpdate() {
        logger.info("Testing status update of book's holding request");
        bookHoldingsDAO.create(testHoldingRequest);
        UpdateBookRequestStatus request = new UpdateBookRequestStatus();
        request.setStatusId(3);
        request.setHoldingRequestId(testHoldingRequest.getId());
        bookHoldingsDAO.changeHoldingStatus(request);

        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
        Assert.assertEquals(request.getStatusId(), holdingRequestFromDb.getStatusId());
    }

    /**
     * Testing getting user's unresponded book requests
     */
    @Test
    public void testGetFreshRequests() {
        logger.info("Testing getting user's unresponded book requests");
        List<BookRequest> requests =
                bookHoldingsDAO.getFreshRequests(1);
        Assert.assertNotNull(requests);
        Assert.assertEquals(1, requests.size());
    }

    /**
     * Testing getting user's approved book requests
     */
    @Test
    public void testGetApprovedRequests() {
        logger.info("Testing getting user's approved book requests");
        List<BookRequest> requests =
                bookHoldingsDAO.getApprovedRequests(4);
        Assert.assertNotNull(requests);
        Assert.assertEquals(1, requests.size());
    }
}
