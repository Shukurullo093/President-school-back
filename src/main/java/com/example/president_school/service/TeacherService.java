package com.example.president_school.service;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Task;
import com.example.president_school.entity.TaskSource;
import com.example.president_school.payload.ControllerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service
public interface TeacherService {
    ControllerResponse addLesson(Integer order, String title, String description, String type, Integer grade,
                                 MultipartFile source);

    ControllerResponse addTest(String lessonId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);

//    VideoSource getVideo(String hashId);

    TaskSource getPdf(String hashId);

    ControllerResponse editLesson(String id, String title, String description, String type, MultipartFile video, MultipartFile task);

    void deleteTest(String id);

    ControllerResponse editTest(String lessonId, Integer testId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);

    void exportLessonToExcel(HttpServletResponse response) throws IOException;

    void exportLessonToPdf(HttpServletResponse response) throws IOException;

    ControllerResponse addTask(UUID lessonId, String taskBody, MultipartFile taskImg, String answer, String exampleBody, MultipartFile exampleImg);

    Lesson getLessonByHashId(String hashId);

    TaskSource viewTaskImg(String hashId);
}
