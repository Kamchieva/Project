package com.example.project.controller;

import com.example.project.dto.UserRegistrationDto;
import com.example.project.model.User;
import com.example.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register"; // Return to the form if there are validation errors
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setPasswordHash(registrationDto.getPassword());
        userService.registerNewUser(newUser);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
