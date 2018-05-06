package com.bookinator.api;

import com.bookinator.api.dao.BookHoldingsDAO;
import com.bookinator.api.model.HoldingRequest;
import com.bookinator.api.model.dto.UpdateHoldingRequestStatus;
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
 * Created by Sabina on 5/6/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class BookHoldingsDAOTest {
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

    @Test
    public void testCreate() {
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

    @Test
    public void testStatusUpdate() {
        bookHoldingsDAO.create(testHoldingRequest);
        UpdateHoldingRequestStatus request = new UpdateHoldingRequestStatus();
        request.setStatusId(3);
        request.setHoldingRequestId(testHoldingRequest.getId());
        bookHoldingsDAO.changeHoldingStatus(request);

        HoldingRequest holdingRequestFromDb =
                bookHoldingsDAO.getHoldingRequestById(testHoldingRequest.getId());
        Assert.assertEquals(request.getStatusId(), holdingRequestFromDb.getStatusId());
    }

    @Test
    public void testGetFreshRequests() {
        List<com.bookinator.api.model.dto.HoldingRequest> requests =
                bookHoldingsDAO.getFreshRequests(1);
        Assert.assertNotNull(requests);
        Assert.assertEquals(1, requests.size());
    }

    @Test
    public void testGetApprovedRequests() {
        List<com.bookinator.api.model.dto.HoldingRequest> requests =
                bookHoldingsDAO.getFreshRequests(4);
        Assert.assertNotNull(requests);
        Assert.assertEquals(1, requests.size());
    }
}
