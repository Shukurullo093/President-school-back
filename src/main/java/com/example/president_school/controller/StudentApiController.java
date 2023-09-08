package com.example.president_school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class StudentApiController {

    @GetMapping("/register")
    public String register(){
        return "student/register";
    }

    @GetMapping("/login")
    public String login(){
        return "student/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "student/dashboard";
    }

    @GetMapping("/demo")
    public String demo(){
        return "student/demo";
    }

    @GetMapping("/watch")
    public String watch(){
        return "student/watch-video";
    }

    @GetMapping("/update/profile")
    public String updateProfile(){
        return "student/update";
    }

    @GetMapping("/my/course")
    public String myCourses(){
        return "student/myCourses";
    }
}