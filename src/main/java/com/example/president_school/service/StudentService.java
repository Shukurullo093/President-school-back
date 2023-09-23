package com.example.president_school.service;

import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface StudentService {
    ControllerResponse register(StudentDto studentDto);

    String shop(String courseId, String s);

    void exportTestResultToPdf(HttpServletResponse response);
}
