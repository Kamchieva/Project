package com.example.project.controller;

import com.example.project.dto.ChatRequest;
import com.example.project.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenPostToChat_withoutAuthentication_thenReturns401() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage("Hello");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER") // Simulate a logged-in user
    void whenPostToChat_withAuthenticationAndValidRequest_thenReturns200() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage("What is Spring Boot?");

        String aiReply = "Spring Boot is an open source Java-based framework used to create a micro Service.";
        when(chatService.getChatResponse(chatRequest.getMessage())).thenReturn(aiReply);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(aiReply));
    }

    @Test
    @WithMockUser // Simulate a logged-in user
    void whenPostToChat_withInvalidRequest_thenReturns400() throws Exception {
        // Send an empty JSON object, which does not map to the ChatRequest DTO
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
