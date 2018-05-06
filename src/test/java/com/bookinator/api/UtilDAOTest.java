package com.bookinator.api;

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

/**
 * Created by Sabina on 5/1/2018.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class UtilDAOTest {
    @Autowired
    private UtilDAO utilDAO;

    @Test
    public void testGetCountries() {
        List<Util> countries = utilDAO.getCountries();
        Assert.assertNotNull(countries);
    }

    @Test
    public void testGetCities() {
        List<Util> cities = utilDAO.getCities();
        Assert.assertNotNull(cities);
    }

    @Test
    public void testGetFields() {
        List<Util> fields = utilDAO.getFields();
        Assert.assertNotNull(fields);
        Assert.assertEquals(fields.size(), 5);
    }

    @Test
    public void testGetBookStatusTypes() {
        List<Util> statuses = utilDAO.getBookStatusTypes();
        Assert.assertNotNull(statuses);
        Assert.assertEquals(statuses.size(), 5);
    }

    @Test
    public void testGetBookHoldingTypes() {
        List<Util> holdings = utilDAO.getBookHoldingTypes();
        Assert.assertNotNull(holdings);
        Assert.assertEquals(holdings.size(), 2);
    }
}
