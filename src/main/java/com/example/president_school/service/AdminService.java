package com.example.president_school.service;

import com.example.president_school.entity.PersonImage;
import com.example.president_school.entity.Post;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.LoginDto;
import com.example.president_school.payload.StudentDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public interface AdminService {
    PersonImage getImage(String hashId);

    ControllerResponse addEmployee(String name, String surname, String email, String phone,
                                   String science, Date birthdate, String gender, Date joiningDate,
                                   String pass, MultipartFile image, String role, String grade);

    ControllerResponse updateEmployee(String employeeId, String name, String surname, String email, String phone, String science, String role, String grade, Date birthdate, String gender, Date joiningDate, String pass, MultipartFile image);

    ControllerResponse login(LoginDto loginDto);

    ControllerResponse updateAdmin(String name, String surname, String email, String phone, Date birthdate, String gender, String pass, MultipartFile image);

    ControllerResponse addPost(String title, String description, String type, MultipartFile photo);

    Post getPostImage(String hashId);

    ControllerResponse sendHomeMsg(String name, String phone, String message);

    ControllerResponse deleteMsg(String id);

    ControllerResponse updatePost(Integer id, String title, String description, String type, MultipartFile photo);

    void deletePost(Integer id);

    void exportEmployeeToExcel(HttpServletResponse response) throws IOException;

    ControllerResponse addTest(String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);

    StudentDto getStudent(Long studentId);

    void setPermissionToCourse(Long studentId, Integer courseId);

    ControllerResponse addStudent(String fullName, String phone, String grade, String gender, String password);

    ControllerResponse editTest(Integer testId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img);

    void deleteTestById(Integer testId);
}
