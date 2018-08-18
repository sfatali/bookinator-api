package com.bookinator.api.controller.helper;

import com.bookinator.api.model.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Sabina on 8/10/2018.
 */
public class LoginHelper {
    /**
     * For obtaining token while testing other controller's methods
     * that require authorization
     *
     * @param testInstance
     * @param mockMvc
     * @param jacksonLoginTester
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static String getToken(Object testInstance, MockMvc mockMvc,
                                   JacksonTester<LoginRequest> jacksonLoginTester,
                                  String username, String password) throws Exception {
        JacksonTester.initFields(testInstance, new ObjectMapper());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        MockHttpServletResponse response
                = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonLoginTester.write(loginRequest).getJson()))
                .andReturn().getResponse();
        return response.getHeader("Authorization");
    }
}
