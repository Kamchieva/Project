package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection for API requests, which is common and often necessary for stateless REST APIs.
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                // Configures authorization for incoming HTTP requests.
                .authorizeHttpRequests(authorize -> authorize
                        // Permits all requests to the /register and /login endpoints without authentication.
                        .requestMatchers("/", "/home", "/register", "/login", "/h2-console/**").permitAll()
                        // Requires authentication for any other request.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}