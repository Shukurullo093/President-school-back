package com.example.president_school.controller;

import com.example.president_school.entity.PersonImage;
import com.example.president_school.entity.Post;
import com.example.president_school.entity.TaskSource;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.TaskSourceRepository;
import com.example.president_school.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("/api/admin/rest")
@RequiredArgsConstructor
public class AdminRestController {
    private final AdminService adminService;
    private final TaskSourceRepository taskSourceRepository;

    @Value("${upload.folder}")
    private String uploadFolder;

    @PostMapping(value = "/add/employee", produces = "application/json")
    public ResponseEntity<ControllerResponse> addEmployee(@RequestParam("name") String name,
                                                          @RequestParam("surname") String surname,
                                                          @RequestParam("email") String email,
                                                          @RequestParam("phone") String phone,
                                                          @RequestParam("science") String science,
                                                          @RequestParam("birthDate") Date birthdate,
                                                          @RequestParam("gender") String gender,
                                                          @RequestParam("joiningDate") Date joiningDate,
                                                          @RequestParam("role") String role,
                                                          @RequestParam("class") String grade,
                                                          @RequestParam("pass") String pass,
                                                          @RequestParam("image")MultipartFile image){
        return ResponseEntity.ok(adminService.addEmployee(name, surname, email, phone, science, birthdate,
                gender, joiningDate, pass, image, role, grade));
    }

    @PostMapping(value = "/update/employee/{employeeId}", produces = "application/json")
    public ResponseEntity<ControllerResponse> updateEmployee(@PathVariable String employeeId,
                                                             @RequestParam("name") String name,
                                                              @RequestParam("surname") String surname,
                                                              @RequestParam("email") String email,
                                                              @RequestParam("phone") String phone,
                                                              @RequestParam("science") String science,
                                                              @RequestParam("birthDate") Date birthdate,
                                                              @RequestParam("gender") String gender,
                                                              @RequestParam("joiningDate") Date joiningDate,
                                                              @RequestParam("role") String role,
                                                              @RequestParam("grade") String grade,
                                                              @RequestParam("pass") String pass,
                                                              @RequestParam("image")MultipartFile image){
        return ResponseEntity.ok(adminService.updateEmployee(employeeId, name, surname, email, phone, science,
               role, grade, birthdate, gender, joiningDate, pass, image));
    }

    @PostMapping(value = "/update/admin")
    public ResponseEntity<ControllerResponse> updateAdmin(@RequestParam("name") String name,
                                                             @RequestParam("surname") String surname,
                                                             @RequestParam("email") String email,
                                                             @RequestParam("phone") String phone,
                                                             @RequestParam("birthDate") Date birthdate,
                                                             @RequestParam("gender") String gender,
                                                             @RequestParam("pass") String pass,
                                                             @RequestParam("image")MultipartFile image){
        return ResponseEntity.ok(adminService.updateAdmin(name, surname, email, phone, birthdate, gender, pass, image));
    }

    @GetMapping("/viewImage/{hashId}")
    public ResponseEntity<?> viewImage(@PathVariable String hashId) throws IOException {
        PersonImage image = adminService.getImage(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }

    @GetMapping("/pdf/{hashId}")
    public ResponseEntity<?> viewPdf(@PathVariable String hashId) throws IOException {
        TaskSource lessonSource = taskSourceRepository.findByHashId(hashId).get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(lessonSource.getName()))
                .contentType(MediaType.parseMediaType(lessonSource.getContentType()))
                .contentLength(lessonSource.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, lessonSource.getUploadPath())));
    }

    @PostMapping("/add/post")
    public ResponseEntity<ControllerResponse> createPost(@RequestParam("title")String title,
                                                         @RequestParam("description")String description,
                                                         @RequestParam("type")String type,
                                                         @RequestParam("photo")MultipartFile photo){
        return ResponseEntity.ok(adminService.addPost(title, description, type, photo));
    }

    @PutMapping("/edit/post/{id}")
    public ResponseEntity<ControllerResponse> updatePost(@PathVariable Integer id,
                                                         @RequestParam("title")String title,
                                                         @RequestParam("description")String description,
                                                         @RequestParam("type")String type,
                                                         @RequestParam("photo")MultipartFile photo){
        return ResponseEntity.ok(adminService.updatePost(id, title, description, type, photo));
    }

    @DeleteMapping("/delete/post/{id}")
    public void deletePost(@PathVariable Integer id){
        adminService.deletePost(id);
    }

    @GetMapping("/post/image/{hashId}")
    public ResponseEntity<?> getPostImage(@PathVariable String hashId) throws IOException {
        Post image = adminService.getPostImage(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }

    @PostMapping("/home/message")
    public ResponseEntity<ControllerResponse> sendHomeMsg(@RequestParam("name")String name,
                                                          @RequestParam("phone")String phone,
                                                          @RequestParam("message")String message){
        return ResponseEntity.ok(adminService.sendHomeMsg(name, phone, message));
    }

    @DeleteMapping("/delete/msg/{id}")
    public ResponseEntity<?> deleteMsg(@PathVariable String id){
        return ResponseEntity.ok(adminService.deleteMsg(id));
    }

    @GetMapping("/export/employee/excel")
    public void exportEmployeeToExcel(HttpServletResponse response) throws IOException {
        adminService.exportEmployeeToExcel(response);
    }

    @PostMapping("/add-iq-test")
    public ResponseEntity<?> addTest(@RequestParam("questionTxt")String question,
                                     @RequestParam("questionImg")MultipartFile questionImg,
                                     @RequestParam("v1")String ans1,
                                     @RequestParam("v1img")MultipartFile ans1Img,
                                     @RequestParam("v2")String ans2,
                                     @RequestParam("v2img")MultipartFile ans2Img,
                                     @RequestParam("v3")String ans3,
                                     @RequestParam("v3img")MultipartFile ans3Img){
        return ResponseEntity.ok(adminService.addTest(question, questionImg, ans1, ans1Img, ans2, ans2Img, ans3, ans3Img));
    }

    @PutMapping("/edit-iq-test/{testId}")
    public ResponseEntity<ControllerResponse> editTest(@PathVariable Integer testId,
                                                       @RequestParam("questionTxt")String question,
                                                       @RequestParam("questionImg")MultipartFile questionImg,
                                                       @RequestParam("v1")String ans1,
                                                       @RequestParam("v1img")MultipartFile ans1Img,
                                                       @RequestParam("v2")String ans2,
                                                       @RequestParam("v2img")MultipartFile ans2Img,
                                                       @RequestParam("v3")String ans3,
                                                       @RequestParam("v3img")MultipartFile ans3Img){
        return ResponseEntity.ok(adminService.editTest(testId, question, questionImg, ans1, ans1Img, ans2, ans2Img, ans3, ans3Img));
    }

    @DeleteMapping("/delete-test/{testId}")
    public void deleteTestById(@PathVariable Integer testId){
        adminService.deleteTestById(testId);
    }

    @GetMapping("/get/student/{studentId}")
    public StudentDto getStudent(@PathVariable Long studentId){
        return adminService.getStudent(studentId);
    }

    @PostMapping("/set-permission-to-course/{studentId}/{courseId}")
    public void setPermissionToCourse(@PathVariable Long studentId, @PathVariable Integer courseId){
        adminService.setPermissionToCourse(studentId, courseId);
    }

    @PostMapping("/add/student")
    public ResponseEntity<ControllerResponse> addStudent(@RequestParam("fullName")String fullName, @RequestParam("phone")String phone,
                                                         @RequestParam("grade")String grade, @RequestParam("gender")String gender,
                                                         @RequestParam("pass")String password){
        return ResponseEntity.ok(adminService.addStudent(fullName, phone, grade, gender, password));
    }
}
