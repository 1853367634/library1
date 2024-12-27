package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user_profile";
    }

    @GetMapping("/manage_books")
    public String manageBooks() {
        return "manage_books";
    }

    @GetMapping("/user_manage")
    public String userManage() {
        return "user_manage";
    }

    @GetMapping("/borrow_history")
    public String borrowHistory() {
        return "borrow_history";
    }
} 