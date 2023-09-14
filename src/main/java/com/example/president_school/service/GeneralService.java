package com.example.president_school.service;

import com.example.president_school.entity.Student;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GeneralService {
    private final StudentRepository studentRepository;

    public EmployeeDto getProfile(String phone){
        Optional<Student> byPhone = studentRepository.findByPhone(phone);
        EmployeeDto employeeDto = new EmployeeDto();
        if (byPhone.isPresent()) {
            Student student = byPhone.get();
            employeeDto.setFullName(student.getFullName());
            employeeDto.setGrade(String.valueOf(student.getGrade()));
            if (student.getImage() != null) {
                employeeDto.setImageHashId("/api/admin/rest/viewImage/" + student.getImage().getHashId());
            }
            employeeDto.setImageHashId("/images/profile/education/pic-6.jpg");
        }
        return employeeDto;
    }

    public String getExtension(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }
}
