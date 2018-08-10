package com.bookinator.api.dao;

import com.bookinator.api.dao.MessageDAO;
import com.bookinator.api.model.Message;
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
import java.util.logging.Logger;

/**
 * Created by Sabina on 5/6/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class MessageDAOTest {
    private static final Logger logger =
            Logger.getLogger(MessageDAOTest.class.getSimpleName());
    @Autowired
    private MessageDAO messageDAO;
    private Message testMessage;

    @Before
    public void init() {
        testMessage = new Message();
        testMessage.setHoldingRequestId(2);
        testMessage.setSenderId(3);
        testMessage.setReceiverId(4);
        testMessage.setMessageText("the book is awesome");
    }

    /**
     * Testing message creation
     */
    @Test
    public void testCreate() {
        logger.info("Testing message creation");
        messageDAO.create(testMessage);
        Message messageFromDb = messageDAO.getMessageById(testMessage.getId());
        Assert.assertNotNull(messageFromDb);
        Assert.assertEquals(testMessage.getMessageText(), messageFromDb.getMessageText());
        Assert.assertEquals(testMessage.getReceiverId(), messageFromDb.getReceiverId());
        Assert.assertEquals(testMessage.getSenderId(), messageFromDb.getSenderId());
        Assert.assertEquals(testMessage.getHoldingRequestId(), messageFromDb.getHoldingRequestId());
    }

    /**
     * Testing message update
     */
    @Test
    public void testGetMessages() {
        logger.info("Testing message update");
        List<com.bookinator.api.model.dto.Message> messages = messageDAO.getMessages(2);
        Assert.assertNotNull(messages);
        Assert.assertEquals(6, messages.size());
    }
}
