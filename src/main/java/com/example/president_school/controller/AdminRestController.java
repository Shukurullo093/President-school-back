package com.example.president_school.controller;

import com.example.president_school.entity.PersonImage;
import com.example.president_school.entity.TaskSource;
import com.example.president_school.entity.VideoSource;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.TaskSourceRepository;
import com.example.president_school.repository.VideoSourceRepository;
import com.example.president_school.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("/api/admin/rest")
@RequiredArgsConstructor
public class AdminRestController {
    private final EmployeeService employeeService;
    private final VideoSourceRepository videoSourceRepository;
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
        return ResponseEntity.ok(employeeService.addEmployee(name, surname, email, phone, science, birthdate,
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
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, name, surname, email, phone, science,
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
        return ResponseEntity.ok(employeeService.updateAdmin(name, surname, email, phone, birthdate, gender, pass, image));
    }

    @GetMapping("/viewImage/{hashId}")
    public ResponseEntity<?> viewImage(@PathVariable String hashId) throws IOException {
        PersonImage image = employeeService.getImage(hashId);
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

    @DeleteMapping("/delete/employee/{id}")
    public ResponseEntity<ControllerResponse> deleteEmployee(@PathVariable String id){
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }
}
