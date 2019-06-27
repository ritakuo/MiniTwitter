package com.spring.twitterapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/")
    public String homePage() {
        return "Mini Twitter Server";
    }
}
