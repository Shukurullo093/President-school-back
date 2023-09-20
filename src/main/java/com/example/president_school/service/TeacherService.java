package com.example.president_school.service;

import com.example.president_school.entity.TaskSource;
import com.example.president_school.entity.VideoSource;
import com.example.president_school.payload.ControllerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface TeacherService {
    ControllerResponse addLesson(String title, String description, String type, Integer grade,
                                 MultipartFile source, MultipartFile task);

    ControllerResponse addTest(String lessonId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);

    VideoSource getVideo(String hashId);

    TaskSource getPdf(String hashId);

    ControllerResponse editLesson(String id, String title, String description, String type, MultipartFile video, MultipartFile task);

    void deleteTest(String id);

    ControllerResponse editTest(String lessonId, Integer testId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);
}
