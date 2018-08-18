package com.bookinator.api.controller;

import com.bookinator.api.controller.helper.LoginHelper;
import com.bookinator.api.model.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Sabina on 8/10/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HomeLinksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<LoginRequest> jacksonLoginTester;

    /**
     * Runs before every test
     */
    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    /**
     * Success case
     * @throws Exception
     */
    @Test
    public void goHomeSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/home")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        String responseString = response.getContentAsString();
        assertThat(responseString).isNotEmpty();

        JSONObject responseJson = new JSONObject(responseString);
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));

        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));
        Assert.assertTrue("Checking explore is there", linksJson.has("explore"));
        Assert.assertTrue("Checking book-posts is there", linksJson.has("book-posts"));
        Assert.assertTrue("Checking wishlist is there", linksJson.has("wishlist"));
        Assert.assertTrue("Checking profile is there", linksJson.has("profile"));
        Assert.assertTrue("Checking book-requests is there", linksJson.has("book-requests"));
        Assert.assertTrue("Checking book-holdings is there", linksJson.has("book-holdings"));
        Assert.assertTrue("Checking logout is there", linksJson.has("logout"));

        // Checking self link internals:
        JSONObject homeJson = linksJson.getJSONObject("self");
        Assert.assertEquals(homeJson.getString("href"), linkTo(HomeLinksController.class)
                .slash("/johndoe/home").toString());
        Assert.assertEquals(homeJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(homeJson.getBoolean("authRequired"));

        // Checking explore link internals:
        JSONObject exploreJson = linksJson.getJSONObject("explore");
        Assert.assertEquals(exploreJson.getString("href"), linkTo(methodOn(ExploreController.class)
                .authFilterBooks(null, null, null, null,
                        null, null, "johndoe", "token"))
                .toString());
        Assert.assertEquals(exploreJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(exploreJson.getBoolean("authRequired"));
        assertThat(exploreJson.getJSONArray("urlTemplate")).isNotNull();
        Assert.assertEquals(exploreJson.getJSONArray("urlTemplate").length(), 6);

        // Checking book-posts link internals:
        JSONObject bookPostsJson = linksJson.getJSONObject("book-posts");
        Assert.assertEquals(bookPostsJson.getString("href"), linkTo(methodOn(BookController.class)
                .getUsersBooks("johndoe", "token")).toString());
        Assert.assertEquals(bookPostsJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(bookPostsJson.getBoolean("authRequired"));

        // Checking wishlist link internals:
        JSONObject wishlistJson = linksJson.getJSONObject("wishlist");
        Assert.assertEquals(wishlistJson.getString("href"), linkTo(methodOn(WishlistController.class)
                .getUserWishlist("johndoe", "token")).toString());
        Assert.assertEquals(wishlistJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(wishlistJson.getBoolean("authRequired"));

        // Checking profile link internals:
        JSONObject profileJson = linksJson.getJSONObject("profile");
        Assert.assertEquals(profileJson.getString("href"), linkTo(methodOn(UserProfileController.class)
                .getProfile("johndoe", "token")).toString());
        Assert.assertEquals(profileJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(profileJson.getBoolean("authRequired"));

        //Checking book-requests link internals:
        JSONObject reqJson = linksJson.getJSONObject("book-requests");
        Assert.assertEquals(reqJson.getString("href"),
                linkTo(methodOn(BookRequestController.class)
                .getRequests("token", "johndoe")).toString());
        Assert.assertEquals(reqJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(reqJson.getBoolean("authRequired"));

        //Checking book-requests link internals:
        JSONObject holdJson = linksJson.getJSONObject("book-holdings");
        Assert.assertEquals(holdJson.getString("href"),
                linkTo(methodOn(BookHoldingController.class)
                        .getHoldings("token", "johndoe")).toString());
        Assert.assertEquals(holdJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(holdJson.getBoolean("authRequired"));

        //Checking book-requests link internals:
        JSONObject logoutJson = linksJson.getJSONObject("logout");
        Assert.assertEquals(logoutJson.getString("href"), "http://localhost/welcome");
        Assert.assertEquals(logoutJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(logoutJson.getBoolean("authRequired"));
    }

    /**
     * No token case
     * @throws Exception
     */
    @Test
    public void goHomeWithoutToken() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * Wrong HTTP method case
     * @throws Exception
     */
    @Test
    public void goHomeInvalidHttpMethod() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/johndoe/home")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }
}
