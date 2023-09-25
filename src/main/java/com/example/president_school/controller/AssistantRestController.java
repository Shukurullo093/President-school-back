package com.example.president_school.controller;

import com.example.president_school.entity.Chat;
import com.example.president_school.entity.ChatMessageImage;
import com.example.president_school.entity.Employee;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.service.AssistantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@RestController
@RequestMapping("/api/assistant/rest")
public class AssistantRestController {
    private final AssistantService assistantService;
    private final EmployeeRepository employeeRepository;

    @Value("${upload.folder}")
    private String uploadFolder;

    public AssistantRestController(AssistantService assistantService, EmployeeRepository employeeRepository) {
        this.assistantService = assistantService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/send/{task}")
    public ResponseEntity<?> sendMsgToStudent(@PathVariable Integer task,
                                              @RequestParam("messageTxt")String text, @RequestParam("messageImg") MultipartFile photo){
        final Optional<Employee> byPhone = employeeRepository.findByPhone("+");
        return ResponseEntity.ok(assistantService.sendMsgToStudent(byPhone.get(), task, text, photo));
    }

    @GetMapping("/message/image/{hashId}")
    public ResponseEntity<?> getPostImage(@PathVariable String hashId) throws IOException {
        Chat image = assistantService.getMessageImage(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }
}
