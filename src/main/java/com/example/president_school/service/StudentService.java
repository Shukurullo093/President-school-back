package com.example.president_school.service;

import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {
    ControllerResponse register(StudentDto studentDto);

    String shop(String courseId, String s);
}
