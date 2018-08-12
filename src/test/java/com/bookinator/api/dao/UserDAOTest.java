package com.bookinator.api.dao;

import com.bookinator.api.dao.UserDAO;
//import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import com.bookinator.api.model.User;
import com.bookinator.api.model.dto.UserProfile;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sabina on 4/15/2018.
 */
@RunWith(SpringRunner.class)
// If separate database for testing is needed, this is the way:
@TestPropertySource(locations="classpath:test.properties")
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class UserDAOTest {
    private static final Logger logger =
            Logger.getLogger(UserDAOTest.class.getSimpleName());
    @Autowired
    private UserDAO userDAO;
    User testUser;

    @Before
    public void init() {
        testUser = new User();
        testUser.setCityId(1);
        testUser.setEmail("email");
        testUser.setName("name");
        testUser.setPassword("pwd");
        testUser.setPhone("phone");
        testUser.setSurname("surname");
        testUser.setUsername("username");
    }

    /**
     * Testing user creation
     */
    @Test
    public void create() {
        logger.info("Testing user creation");
        userDAO.create(testUser);
        User userFromDb = userDAO.getById(testUser.getId());
        Assert.assertEquals(userFromDb.getCityId(), testUser.getCityId());
        Assert.assertEquals(userFromDb.getEmail(), testUser.getEmail());
        Assert.assertEquals(userFromDb.getPhone(), testUser.getPhone());
        Assert.assertEquals(userFromDb.getUsername(), testUser.getUsername());
        Assert.assertEquals(userFromDb.getPassword(), testUser.getPassword());
        Assert.assertEquals(userFromDb.getName(), testUser.getName());
        Assert.assertEquals(userFromDb.getSurname(), testUser.getSurname());
    }

    /**
     * Testing user creation attempt with empty name
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void createWithEmptyName() {
        logger.info(" Testing user creation attempt with empty name");
        testUser.setName(null);
        userDAO.create(testUser);
    }

    /**
     * Testing user creation attempt with empty surname
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void createWithEmptySurname() {
        logger.info(" Testing user creation attempt with empty name");
        testUser.setName(null);
        userDAO.create(testUser);
        User userFromDb = userDAO.getById(testUser.getId());
    }

    /**
     * Testing user update
     */
    @Test
    public void update() {
        logger.info("Testing user update");
        userDAO.create(testUser);
        testUser.setPhone("new phone");
        testUser.setEmail("new mail");
        testUser.setName("new name");
        testUser.setSurname("new surname");
        testUser.setCityId(2);
        userDAO.update(testUser);
        User userFromDb = userDAO.getById(testUser.getId());
        Assert.assertEquals(userFromDb.getCityId(), testUser.getCityId());
        Assert.assertEquals(userFromDb.getEmail(), testUser.getEmail());
        Assert.assertEquals(userFromDb.getPhone(), testUser.getPhone());
        Assert.assertEquals(userFromDb.getUsername(), testUser.getUsername());
        Assert.assertEquals(userFromDb.getPassword(), testUser.getPassword());
        Assert.assertEquals(userFromDb.getName(), testUser.getName());
        Assert.assertEquals(userFromDb.getSurname(), testUser.getSurname());
    }

    /**
     * Testing getting user profile
     */
    @Test
    public void getUserProfile() {
        logger.info("Testing getting user profile");
        userDAO.create(testUser);
        UserProfile userFromDb = userDAO.getUserProfile(testUser.getId());
        Assert.assertEquals(userFromDb.getCity(), "Oulu");
        Assert.assertEquals(userFromDb.getEmail(), testUser.getEmail());
        Assert.assertEquals(userFromDb.getPhone(), testUser.getPhone());
        Assert.assertEquals(userFromDb.getName(), testUser.getName());
        Assert.assertEquals(userFromDb.getSurname(), testUser.getSurname());
        Assert.assertEquals(userFromDb.getScore(), 0);
    }

    /**
     * Testing user deletion
     */
    @Test
    public void delete() {
        logger.info("Testing user deletion");
        userDAO.create(testUser);
        userDAO.delete(testUser.getId());
        User userFromDb = userDAO.getById(testUser.getId());
        Assert.assertNull(userFromDb);
    }

    /**
     * Testing getting user by username
     */
    @Test
    public void testCountByUsername() {
        logger.info("Testing getting user by username");
        int count = userDAO.countByUsername("sabina");
        Assert.assertEquals(1, count);
    }
}
