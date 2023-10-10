package com.example.president_school.controller;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Task;
import com.example.president_school.entity.TaskSource;
import com.example.president_school.entity.TestImageSource;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.TaskSourceRepository;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/teacher/rest")
@RequiredArgsConstructor
public class TeacherRestController {
    private final TeacherService teacherService;
    private final TestImgSourceRepository testImgSourceRepository;
    private final TaskSourceRepository taskSourceRepository;


    @Value("${upload.folder}")
    private String uploadFolder;

    @PostMapping("/add/lesson")
    public ResponseEntity<ControllerResponse> addLesson(@RequestParam("lessonCount")Integer order,
                                                        @RequestParam("title")String title,
                                                        @RequestParam("description")String description,
                                                        @RequestParam("lessonType")String type,
                                                        @RequestParam("class")String grade,
                                                        @RequestParam("source")MultipartFile video){
        return ResponseEntity.ok(teacherService.addLesson(order, title, description, type, Integer.valueOf(grade), video));
    }

    @GetMapping("/view-task-img/{hashId}")
    public ResponseEntity<?> viewTaskImage(@PathVariable String hashId) throws IOException {
        TaskSource image = taskSourceRepository.findByHashId(hashId).get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(image.getName()))
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, image.getUploadPath())));
    }

    @PostMapping("/add/{lessonId}/task")
    public ResponseEntity<ControllerResponse> addTask(@PathVariable UUID lessonId, @RequestParam("task-description")String taskBody,
                                                      @RequestParam("task-img-source")MultipartFile taskImg, @RequestParam("task-answer")String answer,
                                                      @RequestParam("task-example-description")String exampleBody, @RequestParam("task-example-img-source")MultipartFile exampleImg){
        return ResponseEntity.ok(teacherService.addTask(lessonId, taskBody, taskImg, answer, exampleBody, exampleImg));
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

    @GetMapping("/view-test-image/{hashId}")
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
        final Lesson lesson = teacherService.getLessonByHashId(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(lesson.getName()))
                .contentType(MediaType.parseMediaType(lesson.getContentType()))
                .contentLength(lesson.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, lesson.getUploadPath())));
    }

    @GetMapping("/view-task-image/{hashId}")
    public ResponseEntity<?> viewTaskImg(@PathVariable String hashId) throws IOException {
        final TaskSource taskSource = teacherService.viewTaskImg(hashId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(taskSource.getName()))
                .contentType(MediaType.parseMediaType(taskSource.getContentType()))
                .contentLength(taskSource.getFileSize())
                .body(new FileUrlResource(String.format("%s/%s", uploadFolder, taskSource.getUploadPath())));
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