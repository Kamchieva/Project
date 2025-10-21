package com.example.project.controller;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.RegistrationRequest;
import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User authenticatedUser = userService.authenticateUser(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );
        
        if (authenticatedUser != null) {
            return "Login successful for user: " + authenticatedUser.getUsername();
        } else {
            return "Invalid credentials.";
        }
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest registrationRequest) {
        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPasswordHash(registrationRequest.getPassword());
        userService.registerNewUser(newUser);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
