package com.example.president_school.controller;

import com.example.president_school.entity.Student;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student/rest")
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<ControllerResponse> register(@RequestParam("name")String fullNAme, @RequestParam("phone")String phone,
                                                       @RequestParam("gender")String gender, @RequestParam("grade")String grade,
                                                       @RequestParam("pass")String password, @RequestParam("photo")MultipartFile image){
        return ResponseEntity.ok(studentService.register(new StudentDto(fullNAme, phone, gender, grade, password, image)));
    }

    @PostMapping("/shop/{courseId}")
    public String shop(@PathVariable String courseId){
        return studentService.shop(courseId, "+998901234568");
    }
}
