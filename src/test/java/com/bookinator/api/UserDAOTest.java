package com.bookinator.api;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Sabina on 4/15/2018.
 */
@RunWith(SpringRunner.class)
// If separate database for testing is needed, this is the way:
//@TestPropertySource(locations="classpath:test.properties")
@MybatisTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@EnableTransactionManagement
public class UserDAOTest {
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

    @Test
    public void create() {
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

    @Test
    public void update() {
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

    @Test
    public void getUserProfile() {
        userDAO.create(testUser);
        UserProfile userFromDb = userDAO.getUserProfile(testUser.getId());
        Assert.assertEquals(userFromDb.getCity(), "Oulu");
        Assert.assertEquals(userFromDb.getEmail(), testUser.getEmail());
        Assert.assertEquals(userFromDb.getPhone(), testUser.getPhone());
        Assert.assertEquals(userFromDb.getName(), testUser.getName());
        Assert.assertEquals(userFromDb.getSurname(), testUser.getSurname());
        Assert.assertEquals(userFromDb.getScore(), 0);
    }

    @Test
    public void delete() {
        userDAO.create(testUser);
        userDAO.delete(testUser.getId());
        User userFromDb = userDAO.getById(testUser.getId());
        Assert.assertNull(userFromDb);
    }

    @Test
    public void testCountByUsername() {
        int count = userDAO.countByUsername("sabina");
        Assert.assertEquals(1, count);
    }
}
