package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Student;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.StudentRepository;
import com.example.president_school.service.StudentService;
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

    @PostMapping("/check/test")
    public ResponseEntity<?> checkTest(@RequestBody String[] result1){
        System.out.println(result1);
        return null;
    }

    @PostMapping("/send/{lessonId}/{taskOrder}")
    public ResponseEntity<?> sendMsg(@PathVariable String lessonId, @PathVariable Integer taskOrder,
                                     @RequestParam("messageTxt")String text, @RequestParam("messageImg") MultipartFile photo){
        final Optional<Student> byPhone = studentRepository.findByPhone("+998901234568");
        return ResponseEntity.ok(studentService.sendMsg(byPhone.get(), lessonId, taskOrder, text, photo));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class TestResultDto{
        private int test_id;
        private int data_id;

    }
}
