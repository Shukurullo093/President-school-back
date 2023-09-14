package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantApiController {
    private final EmployeeRepository employeeRepository;

    @GetMapping("/all/message")
    public String getAllMessage(Model map){
        return "assistant/assistant-dashboard";
    }

    @GetMapping("/message/{studentId}/{lessonId}")
    public String getMessageByStudentAndLesson(Model map){
        return "assistant/assistant-chat";
    }

    @GetMapping("/profile")
    public String getProfile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234568");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        employeeDto.setFullName(employee.getLastName() + ' ' + employee.getFirstName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhone(employee.getPhone());
        employeeDto.setBirthDate(getDate(employee.getBirthDate()));
        employeeDto.setJoiningDate(getDate(employee.getJoiningDate()));
        employeeDto.setScience(employee.getScience().toString());
        switch (employee.getGender()) {
            case "Male" -> employeeDto.setGender("Erkak");
            case "Female" -> employeeDto.setGender("Ayol");
        }
        employeeDto.setRole(employee.getRole().toString());
        employeeDto.setGrade(employee.getGrade());
        employeeDto.setImageHashId(employee.getImage().getHashId());

        map.addAttribute("employee", employeeDto);
        return "assistant/profile";
    }

    @GetMapping("/update/profile")
    public String updateProfile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234568");
        Employee employee1 = employeeOptional.get();
        if (employee1.getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        EmployeeDto employee= new EmployeeDto();
        employee.setId(employee1.getId());
        employee.setFullName(employee1.getLastName() + ' ' + employee1.getFirstName());
        employee.setScience(employee1.getScience().toString());
        employee.setEmail(employee1.getEmail());
        employee.setPhone(employee1.getPhone());
        employee.setGender(employee1.getGender());
        employee.setGrade(employee1.getGrade());
        employee.setRole(String.valueOf(employee1.getRole()));
        employee.setBirthDate(getDateFormat(employee1.getBirthDate()));
        employee.setJoiningDate(getDateFormat(employee1.getJoiningDate()));
        map.addAttribute("employee", employee);
        return "assistant/updateProfile";
    }

    private String getDate(Date date){
        return date.toString().substring(0, date.toString().indexOf(" "));
    }

    private String getDateFormat(Date date) {
        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return monthFormat.format(date);
    }
}
