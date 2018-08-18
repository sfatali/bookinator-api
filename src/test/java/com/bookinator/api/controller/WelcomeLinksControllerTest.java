package com.bookinator.api.controller;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.logging.Logger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Sabina on 8/3/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeLinksControllerTest {
    private static final Logger logger =
            Logger.getLogger(WelcomeLinksControllerTest.class.getSimpleName());
    @Autowired
    private MockMvc mockMvc;

    /**
     * Welcome - success
     * @throws Exception
     */
    @Test
    public void getWelcomeSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(get("/welcome")
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
        Assert.assertTrue("Checking register is there", linksJson.has("register"));
        Assert.assertTrue("Checking login is there", linksJson.has("login"));
        Assert.assertTrue("Checking explore is there", linksJson.has("explore"));

        // Checking self link internals
        JSONObject selfJson = linksJson.getJSONObject("self");
        Assert.assertEquals(selfJson.getString("href"), linkTo(WelcomeLinksController.class)
                .slash("/welcome").toString());
        Assert.assertEquals(selfJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(selfJson.getBoolean("authRequired"));

        // Checking register link internals
        JSONObject regJson = linksJson.getJSONObject("register");
        Assert.assertEquals(regJson.getString("href"), linkTo(UserController.class)
                .slash("/user").toString());
        Assert.assertEquals(regJson.getString("method"), HttpMethod.POST.toString());
        Assert.assertFalse(regJson.getBoolean("authRequired"));

        // Checking login link internals
        JSONObject loginJson = linksJson.getJSONObject("login");
        Assert.assertEquals(loginJson.getString("href"), "http://localhost:8080/login");
        Assert.assertEquals(loginJson.getString("method"), HttpMethod.POST.toString());
        Assert.assertFalse(loginJson.getBoolean("authRequired"));

        // Checking explore link internals
        JSONObject exploreJson = linksJson.getJSONObject("explore");
        Assert.assertEquals(exploreJson.getString("href"), linkTo(methodOn(ExploreController.class)
                .filterBooks(null, null, null, null, null, null))
                .toString());
        Assert.assertEquals(exploreJson.getString("method"), HttpMethod.GET.toString());
        Assert.assertFalse(exploreJson.getBoolean("authRequired"));
        Assert.assertTrue("Checking self urlTemplate is here", exploreJson.has("urlTemplate"));
    }

    /**
     * Invalid HTTP method
     * @throws Exception
     */
    @Test
    public void getWelcomeInvalidHttpMethod() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/welcome")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
