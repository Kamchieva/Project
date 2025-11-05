package com.example.project.service;

import com.example.project.exception.UserAlreadyExistException;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists: " + user.getUsername());
        }
        // Hash the plain-text password before saving
//        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public User authenticateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user ->
                        passwordEncoder.matches(rawPassword, user.getPasswordHash())
                )
                .orElse(null);
    }
}
