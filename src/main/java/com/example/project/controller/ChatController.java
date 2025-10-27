package com.example.project.controller;

import com.example.project.dto.ChatRequest;
import com.example.project.dto.ChatResponse;
import com.example.project.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse handleChatMessage(@RequestBody ChatRequest chatRequest) {
        String userMessage = chatRequest.getMessage();
        String aiReply = chatService.getChatResponse(userMessage);
        return new ChatResponse(aiReply);
    }
}
