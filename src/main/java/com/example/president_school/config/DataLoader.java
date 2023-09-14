package com.example.president_school.config;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final EmployeeRepository employeeRepository;
//    @Autowired
//    PasswordEncoder passwordEncoder;


    @Value(value = "${spring.sql.init.mode}")
    String initMode;

    @Override
    public void run(ApplicationArguments args){
        if (initMode.equals("always")){
            if (!employeeRepository.existsByRole(Role.ADMIN)){
                employeeRepository.save(new Employee(
                        "admin@gmail.com", "+998901234567", "1111", Role.ADMIN, new Date()
                ));
            }
        }
    }
}
