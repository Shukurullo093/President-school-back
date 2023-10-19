package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Student;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.StudentRepository;
import com.example.president_school.service.StudentService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/student/rest")
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

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

    @GetMapping("/test/result/pdf")
    public void exportTestResultToPdf(HttpServletResponse response){
        studentService.exportTestResultToPdf(response);
    }

    @PostMapping("/check-task/{taskId}")
    public ResponseEntity<?> checkTask(@PathVariable Integer taskId, @RequestParam("answer")String answer){
        final Optional<Student> byPhone = studentRepository.findByPhone("+998901234568");
        return ResponseEntity.ok(studentService.checkTask(byPhone.get(), taskId, answer));
    }

    @PostMapping("/check/test/{lessonId}")
    public void checkTest(@PathVariable UUID id, @RequestBody Integer result){
        final Optional<Student> byId = studentRepository.findById(Long.valueOf("+998901234568"));
        studentService.checkTest(byId.get(), id, result);
    }
}
