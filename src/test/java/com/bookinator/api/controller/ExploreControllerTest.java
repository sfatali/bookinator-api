package com.bookinator.api.controller;

import com.bookinator.api.controller.helper.LoginHelper;
import com.bookinator.api.model.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
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
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource(locations="classpath:test.properties")
public class ExploreControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<LoginRequest> jacksonLoginTester;

    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void filterBooksSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(
                        get(String.format("/explore?name=%s", "A Brief History of Time"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        JSONObject responseJson = new JSONObject(response.getContentAsString());

        // Checking embedded:
        Assert.assertTrue("Checking _embedded is there", responseJson.has("_embedded"));
        Assert.assertTrue("Checking books is there",
                responseJson.getJSONObject("_embedded").has("books"));
        JSONArray books = responseJson.getJSONObject("_embedded").getJSONArray("books");
        Assert.assertEquals(books.length(), 1);

        // Checking the book's fields:
        checkTheBook(books.getJSONObject(0));

        // Checking links:
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));

        // Checking self link
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));

        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(methodOn(ExploreController.class)
                .filterBooks("A Brief History of Time", null, null, null, null, null))
                .toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(selfJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking self urlTemplate is here", selfJson.has("urlTemplate"));
        Assert.assertEquals(selfJson.getJSONArray("urlTemplate").length(), 6);

        JSONObject welcomeJson = linksJson.getJSONObject("welcome");
        Assert.assertEquals(welcomeJson.getString("href"), linkTo(WelcomeLinksController.class)
                .slash("/welcome").toString());
        Assert.assertEquals(welcomeJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(welcomeJson.getBoolean("authRequired"));
    }

    public void checkTheBook(JSONObject book) {
        Assert.assertTrue("Checking bookId is there", book.has("bookId"));
        Assert.assertEquals(book.getInt("bookId"), 8);
        Assert.assertTrue("Checking ownerId is there", book.has("ownerId"));
        Assert.assertEquals(book.getInt("ownerId"), 2);
        Assert.assertTrue("Checking ownerAvgRating is there", book.has("ownerAvgRating"));
        Assert.assertEquals(book.getInt("ownerAvgRating"), 0);
        Assert.assertTrue("Checking name is there", book.has("name"));
        Assert.assertEquals(book.getString("name"), "A brief history of time");
        Assert.assertTrue("Checking holdingType is there", book.has("holdingType"));
        Assert.assertEquals(book.getString("holdingType"), "Read and return");
        Assert.assertTrue("Checking status is there", book.has("status"));
        Assert.assertEquals(book.getString("status"), "Available");
        Assert.assertTrue("Checking authors is there", book.has("authors"));
        Assert.assertEquals(book.getJSONArray("authors").length(), 1);
        Assert.assertTrue("Checking yearPublished is there", book.has("yearPublished"));
        Assert.assertEquals(book.getInt("yearPublished"), 1988);
        Assert.assertTrue("Checking field is there", book.has("field"));
        Assert.assertEquals(book.getString("field"), "Scientific literature");
        Assert.assertTrue("Checking topics is there", book.has("topics"));
        Assert.assertEquals(book.getJSONArray("topics").length(), 4);
        Assert.assertTrue("Checking description is there", book.has("description"));
        Assert.assertEquals(book.getString("description"), "Amazing book, guys!");
        Assert.assertTrue("Checking city is there", book.has("city"));
        Assert.assertEquals(book.getString("city"), "Oulu");
        // Checking links:
        Assert.assertTrue("Checking _links is there", book.has("_links"));
        // Checking self link
        JSONObject linksJson = book.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));
        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(methodOn(BookController.class)
                .getBook ("8")).toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(selfJson.getBoolean("authRequired"));
    }

    @Test
    public void filterBooksWrongHttpMethod() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(
                post(String.format("/explore?name=%s", "A Brief History of Time"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void filterBooksAuthorizedSuccess() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        MockHttpServletResponse response
                = this.mockMvc.perform(
                get(String.format("/johndoe/explore?name=%s", "A Brief History of Time"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",
                                token))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        JSONObject responseJson = new JSONObject(response.getContentAsString());

        // Checking embedded:
        Assert.assertTrue("Checking _embedded is there", responseJson.has("_embedded"));
        Assert.assertTrue("Checking books is there",
                responseJson.getJSONObject("_embedded").has("books"));
        JSONArray books = responseJson.getJSONObject("_embedded").getJSONArray("books");
        Assert.assertEquals(books.length(), 1);

        // Checking the book's fields:
        JSONObject book = books.getJSONObject(0);
        checkTheBook(book);
        JSONObject bookLinks = book.getJSONObject("_links");

        // Checking book's links: add-to-wishlist
        Assert.assertTrue("Checking add-to-wishlist is there", bookLinks.has("add-to-wishlist"));
        JSONObject wishJson = bookLinks.getJSONObject("add-to-wishlist");
        Assert.assertEquals(wishJson.getString("href"), linkTo(WishlistController.class)
                .slash("/johndoe/wish").toString());
        Assert.assertEquals(wishJson.getString("method"), HttpMethod.POST.toString());
        Assert.assertTrue(wishJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking requestTemplate is there", wishJson.has("requestTemplate"));
        Assert.assertEquals(wishJson.getJSONArray("requestTemplate").length(), 2);

        // Checking book's links: make-request
        Assert.assertTrue("Checking make-request is there", bookLinks.has("add-to-wishlist"));
        JSONObject reqJson = bookLinks.getJSONObject("make-request");
        Assert.assertEquals(reqJson.getString("href"), linkTo(BookRequestController.class)
                .slash("/johndoe/requests").toString());
        Assert.assertEquals(reqJson.getString("method"), HttpMethod.POST.toString());
        Assert.assertTrue(reqJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking requestTemplate is there", reqJson.has("requestTemplate"));
        Assert.assertEquals(reqJson.getJSONArray("requestTemplate").length(), 5);

        // Checking links:
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));

        // Checking self link
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));

        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(methodOn(ExploreController.class)
                .authFilterBooks("A Brief History of Time", null, null,
                        null, null, null, "johndoe", token))
                .toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(selfJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking self urlTemplate is here", selfJson.has("urlTemplate"));
        Assert.assertEquals(selfJson.getJSONArray("urlTemplate").length(), 6);

        // Checking home link:
        JSONObject homeJson = linksJson.getJSONObject("home");
        Assert.assertEquals(homeJson.getString("href"), linkTo(methodOn(HomeLinksController.class)
                .goHome(token, "johndoe")).toString());
        Assert.assertEquals(homeJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(homeJson.getBoolean("authRequired"));
    }

    @Test
    public void filterBooksAuthorizedWithoutToken() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(
                get(String.format("/johndoe/explore?name=%s", "A Brief History of Time"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
