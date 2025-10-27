package com.example.project.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final AIService aiService;

    public ChatService(AIService aiService) {
        this.aiService = aiService;
    }

    public String getChatResponse(String prompt) {
        // Here you could add more complex business logic, such as:
        // - Storing chat history
        // - Performing sentiment analysis on the prompt
        // - Calling multiple services and combining the results

        // For now, we will just delegate to the AIService.
        return aiService.getSecureResponse(prompt);
    }
}
