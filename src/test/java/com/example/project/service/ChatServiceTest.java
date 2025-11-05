package com.example.project.service;

import com.example.project.dto.chat.AIRequest;
import com.example.project.dto.chat.AIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AIService aiService;

    // The ChatService instance we are testing
    private ChatService chatService;

    private final String FAKE_API_URL = "http://fake-ai-service.com/chat";
    private final String FAKE_API_KEY = "fake-api-key";

    @BeforeEach
    void setUp() {
        // Manually create the ChatService instance with the mocked dependencies
        chatService = new ChatService(restTemplate, aiService, FAKE_API_URL);
        // Mock the behavior of the AIService
        when(aiService.getApiKey()).thenReturn(FAKE_API_KEY);
    }

    @Test
    void whenGetChatResponse_withValidPrompt_thenReturnsSuccessfulReply() {
        // Arrange
        String prompt = "What is Spring Boot?";
        AIResponse mockResponse = new AIResponse();
        mockResponse.setReply("Spring Boot is a framework.");

        // Mock the RestTemplate's postForObject method
        when(restTemplate.postForObject(eq(FAKE_API_URL), any(HttpEntity.class), eq(AIResponse.class)))
                .thenReturn(mockResponse);

        // Act
        String actualReply = chatService.getChatResponse(prompt);

        // Assert
        assertThat(actualReply).isEqualTo("Spring Boot is a framework.");
    }

    @Test
    void whenGetChatResponse_andApiReturnsNull_thenReturnsDefaultMessage() {
        // Arrange
        String prompt = "Hello";
        // Mock the RestTemplate to return a null response
        when(restTemplate.postForObject(eq(FAKE_API_URL), any(HttpEntity.class), eq(AIResponse.class)))
                .thenReturn(null);

        // Act
        String actualReply = chatService.getChatResponse(prompt);

        // Assert
        assertThat(actualReply).isEqualTo("Sorry, I could not get a response.");
    }

    @Test
    void whenGetChatResponse_andApiThrowsError_thenReturnsErrorMessage() {
        // Arrange
        String prompt = "Hello";
        // Mock the RestTemplate to throw an exception
        when(restTemplate.postForObject(eq(FAKE_API_URL), any(HttpEntity.class), eq(AIResponse.class)))
                .thenThrow(new HttpClientErrorException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        String actualReply = chatService.getChatResponse(prompt);

        // Assert
        assertThat(actualReply).isEqualTo("Error: Could not connect to the AI service.");
    }
}
