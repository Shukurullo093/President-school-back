package com.example.president_school.service;

import com.example.president_school.entity.LessonSource;
import com.example.president_school.payload.ControllerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface TeacherService {
    ControllerResponse addLesson(String title, String description, String type, Integer grade,
                                 MultipartFile source, MultipartFile task,
                                 MultipartFile test, String testAnswer);

    LessonSource getVideo(String hashId);

    ControllerResponse editLesson(String id, String title, String description, String type,
                                  MultipartFile source, MultipartFile task, MultipartFile test, String testAnswer);
}
