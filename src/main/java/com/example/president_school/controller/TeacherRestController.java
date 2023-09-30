package com.example.president_school.controller;

import com.example.president_school.entity.TaskSource;
import com.example.president_school.entity.TestImageSource;
import com.example.president_school.entity.VideoSource;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.TestImgSourceRepository;
import com.example.president_school.service.TeacherService;
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

@RestController
@RequestMapping("/api/teacher/rest")
@RequiredArgsConstructor
public class TeacherRestController {
    private final TeacherService teacherService;
    private final TestImgSourceRepository testImgSourceRepository;


    @Value("${upload.folder}")
    private String uploadFolder;

    @PostMapping("/add/lesson")
    public ResponseEntity<ControllerResponse> addLesson(@RequestParam("title")String title,
                                                        @RequestParam("description")String description,
                                                        @RequestParam("lessonType")String type,
                                                        @RequestParam("class")String grade,
                                                        @RequestParam("source")MultipartFile source,
                                                        @RequestParam("task")MultipartFile task){
        return ResponseEntity.ok(teacherService.addLesson(title, description, type, Integer.valueOf(grade), source, task));
    }

    @PostMapping("/add/test/{lessonId}")
    public ResponseEntity<ControllerResponse> addTest(@PathVariable String lessonId,
                                                      @RequestParam("questionTxt")String question,
                                                      @RequestParam("questionImg")MultipartFile questionImg,
                                                      @RequestParam("v1")String ans1,
                                                      @RequestParam("v1img")MultipartFile ans1Img,
                                                      @RequestParam("v2")String ans2,
                                                      @RequestParam("v2img")MultipartFile ans2Img,
                                                      @RequestParam("v3")String ans3,
                                                      @RequestParam("v3img")MultipartFile ans3Img){

        return ResponseEntity.ok(teacherService.addTest(lessonId, question, questionImg, ans1, ans1Img, ans2, ans2Img, ans3, ans3Img));
    }

    @PutMapping("/edit/test/{lessonId}/{testId}")
    public ResponseEntity<ControllerResponse> updateTest(@PathVariable String lessonId,
                                                      @PathVariable Integer testId,
                                                      @RequestParam("questionTxt")String question,
                                                      @RequestParam("questionImg")MultipartFile questionImg,
                                                      @RequestParam("v1")String ans1,
                                                      @RequestParam("v1img")MultipartFile ans1Img,
                                                      @RequestParam("v2")String ans2,
                                                      @RequestParam("v2img")MultipartFile ans2Img,
                                                      @RequestParam("v3")String ans3,
                                                      @RequestParam("v3img")MultipartFile ans3Img){

        return ResponseEntity.ok(teacherService.editTest(lessonId, testId, question, questionImg, ans1, ans1Img, ans2, ans2Img, ans3, ans3Img));
    }

    @GetMapping("/viewImage/{hashId}")
    public ResponseEntity<?> viewImage(@PathVariable String hashId) throws IOException {
        TestImageSource image = testImgSourceRepository.findByHashId(hashId).get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }

    @PostMapping("/edit/lesson/{id}")
    public ResponseEntity<ControllerResponse> editLesson(@PathVariable String id,
                                                        @RequestParam("title")String title,
                                                        @RequestParam("description")String description,
                                                        @RequestParam("lessonType")String type,
                                                        @RequestParam("source") MultipartFile video,
                                                        @RequestParam("task") MultipartFile task){
        return ResponseEntity.ok(teacherService.editLesson(id, title, description, type, video, task));
    }

    @GetMapping("/viewVideo/{hashId}")
    public ResponseEntity<?> viewVideo(@PathVariable String hashId) throws IOException {
        VideoSource video = teacherService.getVideo(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(video.getName()))
                .contentType(MediaType.parseMediaType(video.getContentType()))
                .contentLength(video.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, video.getUploadPath())));
    }

    @GetMapping("/task/{hashId}")
    public ResponseEntity<?> viewPdf(@PathVariable String hashId) throws IOException {
        TaskSource task = teacherService.getPdf(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(task.getName()))
                .contentType(MediaType.parseMediaType(task.getContentType()))
                .contentLength(task.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, task.getUploadPath())));
    }

    @DeleteMapping("/test/{id}")
    public void deleteTest(@PathVariable String id){
        teacherService.deleteTest(id);
    }

    @GetMapping("/export/lesson/excel")
    public void exportLessonToExcel(HttpServletResponse response) throws IOException {
        teacherService.exportLessonToExcel(response);
    }

    @GetMapping("/export/lesson/pdf")
    public void exportLessonToPdf(HttpServletResponse response) throws IOException {
        teacherService.exportLessonToPdf(response);
    }
}