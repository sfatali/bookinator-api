package com.bookinator.api.controller;

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

/**
 * Created by Sabina on 8/9/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableTransactionManagement
@TestPropertySource(locations="classpath:test.properties")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    private LoginRequest loginRequest;
    private JacksonTester<LoginRequest> jacksonTester;

    @Before
    public void init() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("johndoe");
        loginRequest.setPassword("12345678");
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    @Transactional
    public void loginSuccess() throws Exception {
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginRequest).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();

        // checking headers:
        assertThat(response.getHeaders("Authorization").toString())
                .isNotEmpty().contains("Bearer ");

        JSONObject responseJson = new JSONObject(response.getContentAsString());
        Assert.assertTrue("Checking _links is there", responseJson.has("_links"));
        // checking links:
        JSONObject linksJson = responseJson.getJSONObject("_links");
        Assert.assertTrue("Checking self is there", linksJson.has("home"));
    }

    @Test
    public void loginFail() throws Exception {
        loginRequest.setPassword("1234567");
        // when:
        MockHttpServletResponse response
                = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(loginRequest).getJson()))
                .andReturn().getResponse();

        // then:
        assertThat(response.getStatus()).as("Checking HTTP status")
                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEmpty();

        // checking headers:
        assertThat(response.getHeaders("Authorization")).isEmpty();
    }
}
