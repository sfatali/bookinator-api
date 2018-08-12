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
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by Sabina on 8/9/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class UserProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<LoginRequest> jacksonLoginTester;

    @Before
    public void init() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getProfileSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/profile")
                .header("Authorization", LoginHelper.getToken(this, mockMvc,
                        jacksonLoginTester, "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();

        JSONObject responseJson = new JSONObject(response.getContentAsString());
        // Checking user profile:
        Assert.assertEquals("Checking user id",
                responseJson.getInt("userId"), 5);
        Assert.assertEquals("Checking user name",
                responseJson.getString("name"), "John");
        Assert.assertEquals("Checking user surname",
                responseJson.getString("surname"), "Doe");
        Assert.assertEquals("Checking user city",
                responseJson.getString("city"), "Oulu");
        Assert.assertEquals("Checking user phone",
                responseJson.getString("phone"), "+358460000004");
        Assert.assertEquals("Checking user email",
                responseJson.getString("email"), "somemail5@gmail.com");
        Assert.assertEquals("Checking user score",
                responseJson.getInt("score"), 0);

        // Checking links:
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("self"));
        Assert.assertTrue("Checking edit-user is there", linksJson.has("edit-user"));
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
        Assert.assertTrue("Checking reviews is there", linksJson.has("reviews"));

        // Checking self link internals
        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(UserProfileController.class)
                .slash("/johndoe/profile").toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(selfJson.getBoolean("authRequired"));

        // Checking edit-user link internals
        JSONObject editJson = linksJson.getJSONObject("edit-user");
        Assert.assertEquals(editJson.getString("href"), linkTo(UserProfileController.class)
                .slash("/johndoe/user").toString());
        Assert.assertEquals(editJson.getString("method"), HttpMethod.PUT.toString());
        Assert.assertTrue(editJson.getBoolean("authRequired"));
        JSONArray editRequestTemplate = editJson.getJSONArray("requestTemplate");
        assertThat(editRequestTemplate).isNotNull();
        Assert.assertEquals("Checking length of request",
                editRequestTemplate.length(), 5);
        JSONAssert.assertEquals(editRequestTemplate,
                new JSONArray("[\n" +
                        "        {\n" +
                        "          \"field\": \"name\",\n" +
                        "          \"type\": \"String\",\n" +
                        "          \"required\": true,\n" +
                        "          \"minLength\": 2,\n" +
                        "          \"maxLength\": 50\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"field\": \"surname\",\n" +
                        "          \"type\": \"String\",\n" +
                        "          \"required\": true,\n" +
                        "          \"minLength\": 2,\n" +
                        "          \"maxLength\": 50\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"field\": \"cityId\",\n" +
                        "          \"type\": \"int\",\n" +
                        "          \"required\": true,\n" +
                        "          \"minLength\": 1,\n" +
                        "          \"maxLength\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"field\": \"email\",\n" +
                        "          \"type\": \"String\",\n" +
                        "          \"required\": true,\n" +
                        "          \"minLength\": 1,\n" +
                        "          \"maxLength\": null\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"field\": \"phone\",\n" +
                        "          \"type\": \"String\",\n" +
                        "          \"required\": true,\n" +
                        "          \"minLength\": 1,\n" +
                        "          \"maxLength\": null\n" +
                        "        }\n" +
                        "      ]"), JSONCompareMode.STRICT);

        // Checking home link internals:
        JSONObject homeJson = linksJson.getJSONObject("home");
        Assert.assertEquals(homeJson.getString("href"), linkTo(HomeLinksController.class)
                .slash("/johndoe/home").toString());
        Assert.assertEquals(homeJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(homeJson.getBoolean("authRequired"));

        // Checking reviews link internals:
        JSONObject reviewsJson = linksJson.getJSONObject("reviews");
        Assert.assertEquals(reviewsJson.getString("href"), linkTo(ReviewController.class)
                .slash("/johndoe/profile/reviews").toString());
        Assert.assertEquals(reviewsJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertTrue(reviewsJson.getBoolean("authRequired"));
    }

    @Test
    public void getProfileWithoutToken() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/johndoe/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void getProfileEmptyUsername() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/!/profile")
                .header("Authorization", LoginHelper.getToken(this, mockMvc,
                        jacksonLoginTester, "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isNotEmpty().contains("Malformed URL");
    }

    @Test
    public void getProfileUserNotExist() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/testuser/profile")
                .header("Authorization", LoginHelper.getToken(this, mockMvc,
                        jacksonLoginTester, "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isNotEmpty().contains("User does not exist");
    }
}
