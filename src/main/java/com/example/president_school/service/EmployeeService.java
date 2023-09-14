package com.example.president_school.service;

import com.example.president_school.entity.PersonImage;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.LoginDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public interface EmployeeService {
    PersonImage getImage(String hashId);

    ControllerResponse addEmployee(String name,
                                   String surname,
                                   String email,
                                   String phone,
                                   String science,
                                   Date birthdate,
                                   String gender,
                                   Date joiningDate,
                                   String pass,
                                   MultipartFile image,
                                   String role,
                                   String grade);

    ControllerResponse updateEmployee(String employeeId, String name, String surname, String email, String phone, String science, String role, String grade, Date birthdate, String gender, Date joiningDate, String pass, MultipartFile image);

    ControllerResponse deleteEmployee(String id);

    ControllerResponse login(LoginDto loginDto, HttpServletResponse response);

    ControllerResponse updateAdmin(String name, String surname, String email, String phone, Date birthdate, String gender, String pass, MultipartFile image);
}
