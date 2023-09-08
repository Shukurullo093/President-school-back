package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.LessonDto;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.repository.LessonRepository;
import com.example.president_school.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;

    @GetMapping("/dashboard")
    public String getDashboard(Model map, @CurrentUser Employee employee){
        System.out.println("current user " + employee);
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone(employee.getPhone());
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }
        return "admin/dashboard";
    }

    @GetMapping("/add/employee")
    public String addEmployee(Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        return "admin/add-employee";
    }

    @GetMapping("/list/employee")
    public String getEmployees(Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        List<Employee> employeeList = employeeRepository.findByRoleNot(Role.ADMIN);
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee employee : employeeList) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employee.getId());
            employeeDto.setFullName(employee.getLastName() + ' ' + employee.getFirstName());
            employeeDto.setScience(employee.getScience().toString());
            if (employee.getImage()!=null){
                employeeDto.setImageHashId(employee.getImage().getHashId());
            }
            employeeDto.setRole(employee.getRole().toString());
            employeeDto.setGrade(employee.getGrade());
            switch (employee.getGender()) {
                case "Male" -> employeeDto.setGender("Erkak");
                case "Female" -> employeeDto.setGender("Ayol");
            }
            employeeDto.setEmail(employee.getEmail());
            employeeDto.setPhone(employee.getPhone());
            employeeDto.setBirthDate(getDate(employee.getBirthDate()));
            employeeDto.setJoiningDate(getDate(employee.getJoiningDate()));
            employeeDtoList.add(employeeDto);
        }
        map.addAttribute("employeeList", employeeDtoList);
        return "admin/allEmployee";
    }

    private String getDate(Date date){
        return date.toString().substring(0, date.toString().indexOf(" "));
    }

    @GetMapping("/edit/employee/{id}")
    public String editEmployee(@PathVariable String id, Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(Integer.valueOf(id));
        Employee employee1 = employeeOptional.get();

        EmployeeDto employee = new EmployeeDto();
        employee.setId(employee1.getId());
        employee.setFullName(employee1.getLastName() + ' ' + employee1.getFirstName());

        Science science = employee1.getScience();
        employee.setScience(science.name());
        employee.setRole(employee1.getRole().name());
//        System.out.println(employee.getRole());
        employee.setGrade(employee1.getGrade());

        employee.setEmail(employee1.getEmail());
        employee.setPhone(employee1.getPhone());
        employee.setGender(employee1.getGender());
        employee.setGrade(employee1.getGrade());
        employee.setRole(String.valueOf(employee1.getRole()));
        employee.setBirthDate(getDateFormat(employee1.getBirthDate()));
        employee.setJoiningDate(getDateFormat(employee1.getJoiningDate()));
        map.addAttribute("employee", employee);
        return "admin/editEmployee";
    }

    private String getDateFormat(Date date) {
        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return monthFormat.format(date);
    }

    @GetMapping("/about/employee/{id}")
    public String getEmployeeInfo(@PathVariable String id, Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        Optional<Employee> employeeOptional = employeeRepository.findById(Integer.valueOf(id));
        EmployeeDto employeeDto = new EmployeeDto();
        if (employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
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
        }

        map.addAttribute("employee", employeeDto);
        return "admin/employeeProfile";
    }

    @GetMapping("/all/lesson")
    public String getAllLesson(Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        List<Lesson> all = lessonRepository.findAll();
        List<LessonDto> lessonDtoList = new ArrayList<>();
        int i=1;
        for (Lesson lesson : all) {
            lessonDtoList.add(new LessonDto(lesson.getId(), i, lesson.getTitle(), lesson.getLessonType().toString()));
            i++;
        }
        map.addAttribute("all", lessonDtoList);
        return "admin/all-lesson";
    }

    @GetMapping("/lesson/{id}")
    public String getLessonById(@PathVariable String id, Model map){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        Optional<Lesson> lessonOptional = lessonRepository.findById(Integer.valueOf(id));
        Lesson lesson = lessonOptional.get();
        Optional<Employee> employeeOptional = employeeRepository.findById(lesson.getCourse().getEmployee().getId());
        Employee employee = employeeOptional.get();
        map.addAttribute("lessonOwner", new EmployeeDto(employee.getId(), employee.getLastName() + ' ' + employee.getFirstName(),
                null, null, null, null, employee.getScience().toString(), null,
                "/api/admin/rest/viewImage/" + employee.getImage().getHashId(), employee.getRole().toString(), employee.getGrade()));

        LessonDto lessonDto = new LessonDto();

        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setType(lesson.getLessonType().toString());
        lessonDto.setDescription(lesson.getDescription());

        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd-MM-yyyy");
        lessonDto.setCreatedDate(monthFormat.format(lesson.getCreatedDate()));

        if (lessonDto.getType().equals("TEST")){
            lessonDto.setTestAnswer(lesson.getTestAnswer());
            lessonDto.setVideoLink("javascript:void(0);");
            lessonDto.setTaskLink("javascript:void(0);");
            lessonDto.setTestLink("/api/admin/rest/pdf/" + lesson.getLessonTestSource().getHashId());
            lessonDto.setLessonName(lesson.getLessonTestSource().getName());
            lessonDto.setContentType(lesson.getLessonTestSource().getContentType());
            long fileSize = lesson.getLessonTestSource().getFileSize() / (1024 * 1024);
            lessonDto.setSize(Long.toString(fileSize));
        }else {
            lessonDto.setLessonName(lesson.getLessonVideoSource().getName());
            lessonDto.setContentType(lesson.getLessonVideoSource().getContentType());
            long fileSize = lesson.getLessonVideoSource().getFileSize() / (1024 * 1024);
            lessonDto.setSize(Long.toString(fileSize));
            lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + lesson.getLessonVideoSource().getId());
            lessonDto.setTaskLink("/api/admin/rest/pdf/" + lesson.getLessonTaskSource().getHashId());
            lessonDto.setTestLink("javascript:void(0);");
        }

        map.addAttribute("lessonInfo", lessonDto);
        return "admin/about-lesson";
    }

    @GetMapping("/profile")
    public String profile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", null);
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        if (employee.getLastName() == null || employee.getFirstName() == null){
            employeeDto.setFullName("aniqlanmadi");
        }else {
            employeeDto.setFullName(employee.getLastName() + ' ' + employee.getFirstName());
        }
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhone(employee.getPhone());
        if (employee.getBirthDate() == null){
            employeeDto.setBirthDate("aniqlanmadi");
        }else {
            employeeDto.setBirthDate(getDate(employee.getBirthDate()));
        }
        employeeDto.setJoiningDate(getDate(employee.getJoiningDate()));
        if (employee.getGender() == null){
            employeeDto.setGender("aniqlanmadi");
        }else {
            switch (employee.getGender()) {
                case "Male" -> employeeDto.setGender("Erkak");
                case "Female" -> employeeDto.setGender("Ayol");
            }
        }

        employeeDto.setRole(employee.getRole().toString());
        if (employee.getImage() != null)
            employeeDto.setImageHashId(employee.getImage().getHashId());

        map.addAttribute("employee", employeeDto);
        return "admin/profile";
    }

    @GetMapping("/update/profile")
    public String updateProfile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
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
        employee.setEmail(employee1.getEmail());
        employee.setPhone(employee1.getPhone());
        employee.setGender(employee1.getGender());
        if (employee1.getBirthDate() == null){
            employee.setBirthDate(getDateFormat(new Date()));
        }else {
            employee.setBirthDate(getDateFormat(employee1.getBirthDate()));
        }

        map.addAttribute("employee", employee);
        return "admin/updateProfile";
    }
}

// how to redirect another page after token based spring login with jquery