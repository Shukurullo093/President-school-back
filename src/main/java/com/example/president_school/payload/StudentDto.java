package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String fullName;
    private String phone;
    private String gender;
    private String grade;
    private String password;
    private MultipartFile image;
    private String imagePath;
    private String createdDate;
    private List<AccessCourseDto> accessCourseDtoList;
    private String accessCourseDto;

    public StudentDto(String fullName, String phone, String gender, String grade, String password, MultipartFile image) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.grade = grade;
        this.password = password;
        this.image = image;
    }
}
