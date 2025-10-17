package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    // Constructor Injection is the best practice
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        
        // This is where you invoke your method!
        User authenticatedUser = userService.authenticateUser(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );
        
        if (authenticatedUser != null) {
            // Success: Maybe create a session, a JWT token, and return it.
            return "Login successful for user: " + authenticatedUser.getUsername();
        } else {
            // Failure: Return an error response.
            return "Invalid credentials.";
        }
    }
}

// Just a simple class to hold the login data from the request body
class LoginRequest {
    private String username;
    private String password;
    // Getters and setters...


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}