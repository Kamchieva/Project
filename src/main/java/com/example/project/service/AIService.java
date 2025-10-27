package com.example.project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final String apiKey;

    public AIService(@Value("${ai.service.api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        // This method is for demonstration. In a real application, 
        // you would use the apiKey to make requests to the AI service, 
        // not expose it directly.
        return apiKey;
    }

    public String getSecureResponse(String prompt) {
        // In a real application, you would use a library like RestTemplate or WebClient
        // to make a request to the AI service, including the apiKey in the headers.
        System.out.println("Making a secure request to the AI service with the prompt: " + prompt);
        // Simulate receiving a response.
        return "This is a simulated secure response from the AI service for the prompt: '" + prompt + "'";
    }
}
