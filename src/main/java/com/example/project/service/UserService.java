package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Use constructor injection for dependencies
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Hashes the password before saving a new user.
     * @param user The user object to be saved.
     * @return The saved user object.
     */
    public User registerNewUser(User user) {
        // Hash the plain-text password using the injected PasswordEncoder
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);
        return userRepository.save(user);
    }

    /**
     * Authenticates a user by matching the raw password with the stored hashed password.
     * @param username The user's username.
     * @param rawPassword The plain-text password provided by the user.
     * @return The authenticated user if credentials match, otherwise null.
     */
    public User authenticateUser(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return user; // Credentials are correct
        }
        return null; // Authentication failed
    }
}