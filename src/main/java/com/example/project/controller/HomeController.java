package com.example.project.controller;

import com.example.project.dto.UserRegistrationDto;
import com.example.project.model.User;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomeController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Ensure the model has a user object, even on redirect
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegistrationDto());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register"; // Return to the form if there are validation errors
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        // Correctly hash the password before saving
        newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        userService.registerNewUser(newUser);

        // Add a flash attribute to be displayed on the redirected page
        redirectAttributes.addFlashAttribute("successMessage", "HURRRAH !!! Registration successful! You can now log in.");
        return "redirect:/register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
