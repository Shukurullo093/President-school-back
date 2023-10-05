package com.example.president_school.controller;

import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.LoginDto;
//import com.example.president_school.service.AuthService;
import com.example.president_school.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AdminService employeeService;

    @PostMapping("/login")
    public ResponseEntity<ControllerResponse> signIn(@RequestParam("username")String phone,
                                                     @RequestParam("password")String password,
                                                     HttpServletResponse response){
        return ResponseEntity.ok(employeeService.login(new LoginDto(phone, password)));
    }
}
