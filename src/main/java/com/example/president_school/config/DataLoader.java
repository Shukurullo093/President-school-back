package com.example.president_school.config;

import com.example.president_school.entity.Course;
import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Value(value = "${spring.sql.init.mode}")
    String initMode;

    @Override
    public void run(ApplicationArguments args){
        if (initMode.equals("always")){
            if (!employeeRepository.existsByRole(Role.ADMIN)){
                employeeRepository.save(new Employee(
                        "admin@gmail.com", "+998901234567", passwordEncoder.encode("1111"), Role.ADMIN, new Date()
                ));
            }
            if (!lessonRepository.existsByLessonType(LessonType.IQ)){
                lessonRepository.save(new Lesson("IQ test", "description for iq", LessonType.IQ));
            }
        }
    }
}
