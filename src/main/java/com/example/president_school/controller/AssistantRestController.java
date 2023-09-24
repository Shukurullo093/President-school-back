package com.example.president_school.controller;

import com.example.president_school.entity.Chat;
import com.example.president_school.entity.ChatMessageImage;
import com.example.president_school.service.AssistantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/assistant/rest")
public class AssistantRestController {
    private final AssistantService assistantService;

    @Value("${upload.folder}")
    private String uploadFolder;

    public AssistantRestController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/message/image/{hashId}")
    public ResponseEntity<?> getPostImage(@PathVariable String hashId) throws IOException {
        ChatMessageImage image = assistantService.getMessageImage(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }
}
