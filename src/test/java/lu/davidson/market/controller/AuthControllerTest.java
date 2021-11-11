package lu.davidson.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lu.davidson.market.json.LoginRequest;
import lu.davidson.market.json.LoginResult;
import lu.davidson.market.json.User;
import lu.davidson.market.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private  static final String LOGIN ="taoufik";
    private static final String PASSWORD ="pwd";
    private static final  LoginRequest loginRequest = new LoginRequest(LOGIN, PASSWORD);

    @Test
    public void testValidCredentials() throws Exception {
        User user = new User();
        user.setFirstname("taoufik");
        user.setLastname("lastname");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(loginRequest );
        Mockito.when(userService.authenticate(loginRequest)).thenReturn(user);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        LoginResult response = mapper.readValue(responseBody, LoginResult.class);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isConnected());
        Assertions.assertEquals(response.getFirstname(), user.getFirstname());
    }

    @Test
    public void testCurrentAuthenticated() throws Exception {
        User user = new User();
        user.setFirstname("taoufik");
        user.setLastname("lastname");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();


        Mockito.when(userService.retrieveUserFromAuthorization ("Bearer 123")).thenReturn(user);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/auth/current")
        .header("Authorization","Bearer 123"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        User response = objectMapper.readValue(responseBody, User.class);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getFirstname(), user.getFirstname());
    }


}
