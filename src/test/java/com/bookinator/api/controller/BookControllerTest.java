package com.bookinator.api.controller;

import com.bookinator.api.controller.helper.LoginHelper;
import com.bookinator.api.model.Book;

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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

/**
 * Created by Sabina on 8/11/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableTransactionManagement
@TestPropertySource(locations="classpath:test.properties")
public class BookControllerTest {
    private static final Logger logger =
            Logger.getLogger(BookControllerTest.class.getSimpleName());
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<LoginRequest> jacksonLoginTester;
    private JacksonTester<Book> jacksonBookTester;
    private Book book;

    /**
     * Runs before every test
     */
    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());
        book = new Book();
        book.setName("name");
        book.setStatusId(1);
        book.setHoldingTypeId(1);
        book.setOwnerId(5);
        book.setDescription("desc");
        book.setYearPublished(2018);
        book.setFieldId(1);
        book.setAuthors("");
        book.setTopics("meh");
    }

    /**
     * Testing getting book posts - success case
     *
     * @throws Exception
     */
    @Test
    public void getUsersBooksSuccess() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/books")
                .header("Authorization", token)
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
        JSONObject book = books.getJSONObject(0);
        checkPancakesBook(book);
        JSONObject bookLinks = book.getJSONObject("_links");

        // Checking book's links: edit-book
        Assert.assertTrue("Checking edit-book is there", bookLinks.has("edit-book"));
        JSONObject editJson = bookLinks.getJSONObject("edit-book");
        Assert.assertEquals(editJson.getString("href"), linkTo(BookController.class)
                .slash("/johndoe/books/6").toString());
        Assert.assertEquals(editJson.getString("method"), HttpMethod.PUT.toString());
        Assert.assertTrue(editJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking requestTemplate is there", editJson.has("requestTemplate"));
        Assert.assertEquals(editJson.getJSONArray("requestTemplate").length(), 9);

        // Checking book's links: delete-book
        Assert.assertTrue("Checking delete-book is there", bookLinks.has("delete-book"));
        JSONObject delJson = bookLinks.getJSONObject("delete-book");
        Assert.assertEquals(editJson.getString("href"), linkTo(BookController.class)
                .slash("/johndoe/books/6").toString());
        Assert.assertEquals(delJson.getString("method"), HttpMethod.DELETE.toString());
        Assert.assertTrue(delJson.getBoolean("authRequired"));

        // Checking links:
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));

        // Checking self link
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));

        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(methodOn(BookController.class)
                .getUsersBooks("johndoe", token))
                .toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(selfJson.getBoolean("authRequired"));

        // Checking post-book link:
        Assert.assertTrue("Checking post-book is there", linksJson.has("post-book"));
        JSONObject postJson = linksJson.getJSONObject("post-book");
        Assert.assertEquals(postJson.getString("href"), linkTo(BookController.class)
                .slash("/johndoe/books").toString());
        Assert.assertEquals(postJson.getString("method"), HttpMethod.POST.toString());
        Assert.assertTrue(postJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking requestTemplate is there", postJson.has("requestTemplate"));
        Assert.assertEquals(postJson.getJSONArray("requestTemplate").length(), 9);

        // Checking home link:
        JSONObject homeJson = linksJson.getJSONObject("home");
        Assert.assertEquals(homeJson.getString("href"), linkTo(methodOn(HomeLinksController.class)
                .goHome(token, "johndoe")).toString());
        Assert.assertEquals(homeJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(homeJson.getBoolean("authRequired"));
    }

    public void checkPancakesBook(JSONObject book) {
        Assert.assertTrue("Checking bookId is there", book.has("bookId"));
        Assert.assertEquals(book.getInt("bookId"), 6);
        Assert.assertTrue("Checking ownerId is there", book.has("ownerId"));
        Assert.assertEquals(book.getInt("ownerId"), 5);
        Assert.assertTrue("Checking name is there", book.has("name"));
        Assert.assertEquals(book.getString("name"), "Pancakes cookbook");
        Assert.assertTrue("Checking holdingType is there", book.has("holdingType"));
        Assert.assertEquals(book.getString("holdingType"), "Give away");
        Assert.assertTrue("Checking status is there", book.has("status"));
        Assert.assertEquals(book.getString("status"), "Available");
        Assert.assertTrue("Checking authors is there", book.has("authors"));
        Assert.assertEquals(book.getJSONArray("authors").length(), 0);
        Assert.assertTrue("Checking yearPublished is there", book.has("yearPublished"));
        Assert.assertEquals(book.getString("yearPublished"), "");
        Assert.assertTrue("Checking field is there", book.has("field"));
        Assert.assertEquals(book.getString("field"), "Learning and development");
        Assert.assertTrue("Checking topics is there", book.has("topics"));
        Assert.assertEquals(book.getJSONArray("topics").length(), 2);
        Assert.assertTrue("Checking description is there", book.has("description"));
        Assert.assertEquals(book.getString("description"),
                "I used this book to learn how to make pancakes. Results are awesome.");
        // Checking links:
        Assert.assertTrue("Checking _links is there", book.has("_links"));
        // Checking self link
        JSONObject linksJson = book.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));
        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(methodOn(BookController.class)
                .getBook ("6")).toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(selfJson.getBoolean("authRequired"));
    }

    /**
     * Getting certain book - success case
     * @throws Exception
     */
    @Test
    public void getBookSuccess() throws Exception {
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/books/8")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        JSONObject responseJson = new JSONObject(response.getContentAsString());

        // Checking book params:
        checkHawkingBook(responseJson);
    }

    public void checkHawkingBook(JSONObject book) {
        Assert.assertTrue("Checking bookId is there", book.has("bookId"));
        Assert.assertEquals(book.getInt("bookId"), 8);
        Assert.assertTrue("Checking ownerId is there", book.has("ownerId"));
        Assert.assertEquals(book.getInt("ownerId"), 2);
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

    /**
     * Book not found
     * @throws Exception
     */
    @Test
    public void getBookNotFound() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/books/10000")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Invalid path parameter for getting a book post
     * @throws Exception
     */
    @Test
    public void getBookBadRequest() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/books/real_bad")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Create a book post - success case
     * @throws Exception
     */
    @Test
    @Transactional
    public void saveBookSuccess() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/johndoe/books")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonBookTester.write(book).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * Edit book - success case
     * @throws Exception
     */
    @Test
    @Transactional
    public void editBookSuccess() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/books/6")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonBookTester.write(book).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * Edit book that does not exist - new one gets created
     * @throws Exception
     */
    @Test
    @Transactional
    public void editBookNotFoundAndNewCreated() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/books/500")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonBookTester.write(book).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * Delete a book - success case
     * @throws Exception
     */
    @Test
    @Transactional
    public void deleteBookSuccess() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/books/6")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678")))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    /**
     * Trying to delete someone else's book
     * @throws Exception
     */
    @Test
    @Transactional
    public void tryingToDeleteSomeonesBook() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/books/8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678")))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Deleting a book that does not exist
     * @throws Exception
     */
    @Test
    @Transactional
    public void deleteBookNotFound() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/books/7000")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678")))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Trying to edit someone else's book
     * @throws Exception
     */
    @Test
    @Transactional
    public void tryingToEditSomeonesBook() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/books/8")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .content(jacksonBookTester.write(book).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Getting error 400 for invalid parameters in the path
     * @throws Exception
     */
    @Test
    @Transactional
    public void deleteBookBadPath() throws Exception {
        // when:
        String token = LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                "johndoe", "12345678");
        book.setId(8);
        MockHttpServletResponse response
                = this.mockMvc.perform(delete("/johndoe/books/real_bad")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678")))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
