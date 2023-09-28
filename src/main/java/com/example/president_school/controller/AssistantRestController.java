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

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("/send/{student}/{lesson}/{task}")
    public ResponseEntity<?> sendMsgToStudent(@PathVariable Long student, @PathVariable UUID lesson, @PathVariable Integer task,
                                              @RequestParam("messageTxt")String text, @RequestParam("messageImg") MultipartFile photo){
        final Optional<Employee> byPhone = employeeRepository.findByPhone("+998901234568");
        return ResponseEntity.ok(assistantService.sendMsgToStudent(byPhone.get(), student, lesson, task, text, photo));
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

    @GetMapping("/refresh/{student}/{lesson}/{task}")
    public String getMsg(@PathVariable Long student, @PathVariable UUID lesson, @PathVariable Integer task){
        return assistantService.getMsg(student, lesson, task);
    }

    @DeleteMapping("/delete/msg/{id}")
    public ResponseEntity<?> deleteMsg(@PathVariable Integer id){
        return ResponseEntity.ok(assistantService.deleteMsg(id));
    }
}
