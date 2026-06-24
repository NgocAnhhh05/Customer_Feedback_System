package com.uit.se104.feedback_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "dang-ky";
    }

    @GetMapping({"/", "/home"})
    public String home() {
        return "trang-chu";
    }

    @GetMapping("/admin/overview")
    public String adminOverview() {
        return "admin-tong-quan";
    }

    @GetMapping("/feedback/submit")
    public String submitFeedback() {
        return "them-feedback";
    }
}
