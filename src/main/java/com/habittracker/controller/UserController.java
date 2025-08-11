package com.habittracker.controller;
import java.security.Principal;

import com.habittracker.model.User;
import com.habittracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        boolean success = userService.registerUser(user);
        if (!success) {
            return "redirect:/register?error"; // username exists
        }
        return "redirect:/login?registered"; // registration successful
    }
    
    @PostMapping("/delete-account")
    public String deleteAccount(Principal principal) {
        String username = principal.getName();  // get logged-in user
        userService.deleteUserByUsername(username);
        return "redirect:/login?accountDeleted"; // redirect with message
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}

