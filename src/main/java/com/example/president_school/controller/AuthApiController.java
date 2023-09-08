package com.example.president_school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthApiController {
    @GetMapping("/login")
    public String login(){
        return "page-login";
    }
}
