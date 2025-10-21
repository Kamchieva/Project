package com.example.project.controller;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.RegistrationRequest;
import com.example.project.model.User;
import com.example.project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "hashedpassword");
        user.setId(1L);
    }

    @Test
    void whenRegister_withValidInput_thenReturns201() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("newuser");
        registrationRequest.setPassword("password123");

        when(userService.registerNewUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void whenRegister_withInvalidInput_thenReturns400() throws Exception {
        // Sending an empty request body to simulate invalid input
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenLogin_withCorrectCredentials_thenReturns200() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        when(userService.authenticateUser(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful for user: testuser"));
    }

    @Test
    void whenLogin_withIncorrectCredentials_thenReturns200withMessage() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        when(userService.authenticateUser(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid credentials."));
    }

    @Test
    void whenLogin_withNonExistentUser_thenReturns200withMessage() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("password123");

        when(userService.authenticateUser(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid credentials."));
    }
}
