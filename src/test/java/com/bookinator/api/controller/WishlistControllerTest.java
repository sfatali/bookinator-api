package com.bookinator.api.controller;

import com.bookinator.api.controller.helper.LoginHelper;
import com.bookinator.api.model.Wish;
import com.bookinator.api.model.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

/**
 * Created by Sabina on 8/11/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableTransactionManagement
@TestPropertySource(locations="classpath:test.properties")
public class WishlistControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<LoginRequest> jacksonLoginTester;
    private JacksonTester<Wish> jacksonWishlistTester;
    private Wish wish;

    /**
     * Runs before every test
     */
    @Before
    public void init() {
        wish = new Wish();
        wish.setUserId(5);
        wish.setBookId(8);
        JacksonTester.initFields(this, new ObjectMapper());
    }

    /**
     * Getting user wishlist - success case
     * @throws Exception
     */
    @Test
    public void getUserWishlistSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/wishlist")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Getting user wishlist - empty list case
     * @throws Exception
     */
    @Test
    public void getUserWishlistEmptyList() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/alien/wishlist")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "alien", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Trying to access someone else's wishlist
     * @throws Exception
     */
    @Test
    public void accessingSomeoneElsesWishlistForbidden() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/alien/wishlist")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Adding book to wishlist - success
     * @throws Exception
     */
    @Test
    @Transactional
    public void addToWishlistSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/johndoe/wishlist")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonWishlistTester.write(wish).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Trying to add a book that is already in a wishist
     * @throws Exception
     */
    @Test
    public void addToWishlistSecondTime() throws Exception {
        // when:
        wish.setBookId(1);
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/johndoe/wishlist")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonWishlistTester.write(wish).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Removing from wishlist - success
     * @throws Exception
     */
    @Test
    @Transactional
    public void removeFromWishlistSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/wishlist/1")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonWishlistTester.write(wish).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Trying to remove a wish that does not exist
     * @throws Exception
     */
    @Test
    public void removeWishThatNotExist() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/wishlist/100")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonWishlistTester.write(wish).getJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
