package com.example.project.service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password123");
    }

    @Test
    void whenRegisterNewUser_thenPasswordIsHashedAndUserIsSaved() {
        // Arrange
        String rawPassword = user.getPasswordHash();
        String hashedPassword = "hashedPassword";
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate saving by assigning an ID
            return savedUser;
        });

        // Act
        User savedUser = userService.registerNewUser(user);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getPasswordHash()).isEqualTo(hashedPassword);
    }

    @Test
    void whenAuthenticateUser_withCorrectCredentials_thenUserIsReturned() {
        // Arrange
        String rawPassword = "password123";
        user.setPasswordHash("hashedPassword");
        // Mock the repository to return an Optional containing the user
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPasswordHash())).thenReturn(true);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getUsername(), rawPassword);

        // Assert
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void whenAuthenticateUser_withIncorrectCredentials_thenNullIsReturned() {
        // Arrange
        String rawPassword = "wrongPassword";
        // Mock the repository to return an Optional containing the user
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, user.getPasswordHash())).thenReturn(false);

        // Act
        User authenticatedUser = userService.authenticateUser(user.getUsername(), rawPassword);

        // Assert
        assertThat(authenticatedUser).isNull();
    }

    @Test
    void whenAuthenticateUser_withNonExistentUser_thenNullIsReturned() {
        // Arrange
        String username = "nonexistentuser";
        // Mock the repository to return an empty Optional
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        User authenticatedUser = userService.authenticateUser(username, "anypassword");

        // Assert
        assertThat(authenticatedUser).isNull();
    }
}
