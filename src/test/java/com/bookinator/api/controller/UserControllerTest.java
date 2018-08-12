package com.bookinator.api.controller;

import com.bookinator.api.controller.helper.LoginHelper;
import com.bookinator.api.model.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Created by Sabina on 8/9/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableTransactionManagement
@TestPropertySource(locations="classpath:test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private JacksonTester<User> jacksonTester;
    private JacksonTester<LoginRequest> jacksonLoginTester;

    @Before
    public void init() {
        user = new User();
        user.setId(5);
        user.setUsername("username");
        user.setPassword("password");
        user.setName("name");
        user.setSurname("surname");
        user.setCityId(1);
        user.setEmail("email@gmail.com");
        user.setPhone("phone...");
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    @Transactional
    public void registerSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void registerServerError() throws Exception {
        user.setUsername("thatssssaaaaaaaaaaveeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeryyyyyylo" +
                "oooooooooonggggusernameeeeeeeeeeeeeeeeee");
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    public void registerUsernameTaken() throws Exception {
        user.setUsername("sabina");
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.CONFLICT.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    public void registerUsernameEmpty() throws Exception {
        user.setUsername(null);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    public void registerUsernameTooShort() throws Exception {
        user.setUsername("1234");
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    public void registerPasswordEmpty() throws Exception {
        user.setPassword(null);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    public void registerInvalidCity() throws Exception {
        user.setCityId(-100);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // checking links:
        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking home is there", linksJson.has("home"));
    }

    @Test
    @Transactional
    public void editUserSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();
        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void editUserWithoutToken() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void editUserWrongUserId() throws Exception {
        user.setId(2);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).contains("You cannot edit someone else's profile");
    }

    @Test
    public void editUserWrongPath() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe2/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).contains("You cannot edit someone else's profile");
    }

    @Test
    public void editUserEmptyName() throws Exception {
        user.setName(null);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Name cannot be empty");
    }

    @Test
    public void editUserEmptySurname() throws Exception {
        user.setSurname(null);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).contains("Surname cannot be empty");
    }

    @Test
    public void editUserInvalidCity() throws Exception {
        user.setCityId(-101);
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(put("/johndoe/user")
                .header("Authorization",
                        LoginHelper.getToken(this, mockMvc, jacksonLoginTester,
                                "johndoe", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(user).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
