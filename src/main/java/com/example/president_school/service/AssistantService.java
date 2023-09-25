package com.example.president_school.service;

import com.example.president_school.entity.Chat;
import com.example.president_school.entity.Employee;
import com.example.president_school.payload.ControllerResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AssistantService {
    Chat getMessageImage(String hashId);

    ControllerResponse sendMsgToStudent(Employee employee, Integer task, String text, MultipartFile photo);
}
