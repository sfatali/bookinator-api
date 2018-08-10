package com.bookinator.api.dao;

import com.bookinator.api.dao.UtilDAO;
import com.bookinator.api.model.Util;
import org.junit.Assert;
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
 * Created by Sabina on 5/1/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class UtilDAOTest {
    private static final Logger logger =
            Logger.getLogger(UtilDAOTest.class.getSimpleName());
    @Autowired
    private UtilDAO utilDAO;

    /**
     * Testing getting all countries
     */
    @Test
    public void testGetCountries() {
        logger.info("Testing getting all countries");
        List<Util> countries = utilDAO.getCountries();
        Assert.assertNotNull(countries);
    }

    /**
     * Testing getting all cities
     */
    @Test
    public void testGetCities() {
        logger.info("Testing getting all cities");
        List<Util> cities = utilDAO.getCities();
        Assert.assertNotNull(cities);
    }

    /**
     * Testing getting all book topic fields
     */
    @Test
    public void testGetFields() {
        logger.info("Testing getting all book topic fields");
        List<Util> fields = utilDAO.getFields();
        Assert.assertNotNull(fields);
        Assert.assertEquals(fields.size(), 6);
    }

    /**
     * Testing getting book's status types
     */
    @Test
    public void testGetBookStatusTypes() {
        logger.info("Testing getting book's status types");
        List<Util> statuses = utilDAO.getBookStatusTypes();
        Assert.assertNotNull(statuses);
        Assert.assertEquals(statuses.size(), 6);
    }

    /**
     * Testing getting book's holding types
     */
    @Test
    public void testGetBookHoldingTypes() {
        logger.info("Testing getting book's holding types");
        List<Util> holdings = utilDAO.getBookHoldingTypes();
        Assert.assertNotNull(holdings);
        Assert.assertEquals(holdings.size(), 3);
    }
}
