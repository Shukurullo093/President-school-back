package com.example.president_school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeApiController {
    @GetMapping("")
    public String home(Model map){
        return "home/index";
    }

    @GetMapping("/news/{id}")
    public String getNewsById(@PathVariable String id, Model map){
        return "home/news";
    }
}
