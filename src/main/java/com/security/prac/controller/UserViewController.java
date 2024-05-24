package com.security.prac.controller;

import com.security.prac.dto.UserResponse;
import com.security.prac.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal User user) {
        UserResponse userResponse = new UserResponse(user);
        model.addAttribute("UserResponse", userResponse);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
