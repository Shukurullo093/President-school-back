package com.example.president_school.controller;

import com.example.president_school.entity.LessonSource;
import com.example.president_school.entity.PersonImage;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.service.TeacherService;
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

@RestController
@RequestMapping("/api/teacher/rest")
@RequiredArgsConstructor
public class TeacherRestController {
    private final TeacherService teacherService;
    @Value("${upload.folder}")
    private String uploadFolder;

    @PostMapping("/add/lesson")
    public ResponseEntity<ControllerResponse> addLesson(@RequestParam("title")String title,
                                                        @RequestParam("description")String description,
                                                        @RequestParam("lessonType")String type,
                                                        @RequestParam("class")String grade,
                                                        @RequestParam("source")MultipartFile source,
                                                        @RequestParam("task")MultipartFile task,
                                                        @RequestParam("test")MultipartFile test,
                                                        @RequestParam("testAnswer")String testAnswer){
        return ResponseEntity.ok(teacherService.addLesson(title, description, type, Integer.valueOf(grade), source, task, test, testAnswer));
    }

    @PostMapping("/edit/lesson/{id}")
    public ResponseEntity<ControllerResponse> editLesson(@PathVariable String id,
                                                        @RequestParam("title")String title,
                                                        @RequestParam("description")String description,
                                                        @RequestParam("lessonType")String type,
                                                        @RequestParam("testAnswer")String testAnswer,
                                                        @RequestParam("source") MultipartFile source,
                                                        @RequestParam("task") MultipartFile task,
                                                        @RequestParam("test") MultipartFile test){
        return ResponseEntity.ok(teacherService.editLesson(id, title, description, type, source, task, test, testAnswer));
    }

    @GetMapping("/viewVideo/{id}")
    public ResponseEntity<?> viewVideo(@PathVariable String id) throws IOException {
        LessonSource source = teacherService.getVideo(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(source.getName()))
                .contentType(MediaType.parseMediaType(source.getContentType()))
                .contentLength(source.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, source.getUploadPath())));
    }
}
