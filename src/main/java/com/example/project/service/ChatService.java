package com.example.project.service;

import com.example.project.dto.chat.AIRequest;
import com.example.project.dto.chat.AIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {

    private final RestTemplate restTemplate;
    private final String aiServiceUrl;
    private final String apiKey;

    public ChatService(RestTemplate restTemplate, 
                       AIService aiService,
                       @Value("${ai.service.url}") String aiServiceUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = aiService.getApiKey();
        this.aiServiceUrl = aiServiceUrl;
    }

    public String getChatResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        AIRequest requestBody = new AIRequest(prompt);
        HttpEntity<AIRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            AIResponse response = restTemplate.postForObject(aiServiceUrl, entity, AIResponse.class);
            if (response != null && response.getReply() != null) {
                return response.getReply();
            }
            return "Sorry, I could not get a response.";
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error calling AI service: " + e.getMessage());
            return "Error: Could not connect to the AI service.";
        }
    }
}
