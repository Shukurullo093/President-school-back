package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.LessonDto;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.repository.LessonRepository;
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
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherApiController {
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;

    @GetMapping("/dashboard")
    public String getDashboard(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        List<Lesson> lessonList2 = lessonRepository.findAllByCourseGradeAndCourseEmployee(2, employeeOptional.get());
        map.addAttribute("grade2Count", lessonList2.size());

        List<Lesson> lessonList3 = lessonRepository.findAllByCourseGradeAndCourseEmployee(3, employeeOptional.get());
        map.addAttribute("grade3Count", lessonList3.size());

        List<Lesson> lessonList4 = lessonRepository.findAllByCourseGradeAndCourseEmployee(4, employeeOptional.get());
        map.addAttribute("grade4Count", lessonList4.size());
        return "teacher/teacher-dashboard";
    }

    @GetMapping("/add/lesson/{grade}")
    public String addLesson(@PathVariable String grade, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }
        List<Lesson> lessonListByGrade = lessonRepository.findAllByCourseGradeAndCourseEmployee(Integer.valueOf(grade), employeeOptional.get());
        map.addAttribute("gradeCount", lessonListByGrade.size() + 1);
        map.addAttribute("science", employeeOptional.get().getScience().toString());
        map.addAttribute("grade", grade);
        if ((lessonListByGrade.size() + 1) % 7 == 0){
            map.addAttribute("lessonType", "Test");
        }
        else {
            map.addAttribute("lessonType", "Video");
        }

        return "teacher/add-lesson";
    }

    @GetMapping("/all/lesson")
    public String getAllLesson(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        List<Lesson> lessonList = lessonRepository.findAllByCourseEmployeeOrderByCreatedDateAsc(employeeOptional.get());
        List<LessonDto> lessonDtoList2 = new ArrayList<>();
        List<LessonDto> lessonDtoList3 = new ArrayList<>();
        List<LessonDto> lessonDtoList4 = new ArrayList<>();

        int i2=1, i3=1, i4=1;
        for (Lesson lesson : lessonList) {
            if (lesson.getCourse().getGrade() == 2){
                lessonDtoList2.add(new LessonDto(lesson.getId(), i2, lesson.getTitle(), lesson.getLessonType().toString()));
                i2++;
            }
            else if (lesson.getCourse().getGrade() == 3){
                lessonDtoList3.add(new LessonDto(lesson.getId(), i3, lesson.getTitle(), lesson.getLessonType().toString()));
                i3++;
            }
            else {
                lessonDtoList3.add(new LessonDto(lesson.getId(), i4, lesson.getTitle(), lesson.getLessonType().toString()));
                i4++;
            }
        }
        map.addAttribute("gradeStatus", "all");
        map.addAttribute("grade2", lessonDtoList2);
        map.addAttribute("grade3", lessonDtoList3);
        map.addAttribute("grade4", lessonDtoList4);

        return "teacher/all-lesson";
    }

    @GetMapping("/list/lesson/{grade}")
    public String getLessonByClass(@PathVariable String grade, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        List<Lesson> lessonList = lessonRepository.findAllByCourseGradeAndCourseEmployee(Integer.valueOf(grade), employeeOptional.get());
        List<LessonDto> lessonDtoList = new ArrayList<>();

        int i=1;
        for (Lesson lesson : lessonList) {
            lessonDtoList.add(new LessonDto(lesson.getId(), i, lesson.getTitle(), lesson.getLessonType().toString()));
            i++;
        }
        map.addAttribute("gradeStatus", grade);
        map.addAttribute("grade"+grade, lessonDtoList);

        return "teacher/all-lesson";
    }

    @GetMapping("/lesson/info/{id}")
    public String getLessonInfo(@PathVariable String id, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }
        Optional<Lesson> lessonOptional = lessonRepository.findById(Integer.valueOf(id));
        map.addAttribute("lessonInfo", lessonOptional.get());
        return "teacher/lesson-info";
    }

    @GetMapping("/edit/lesson/{id}")
    public String editLesson(@PathVariable String id, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        Optional<Lesson> lessonOptional = lessonRepository.findById(Integer.valueOf(id));
        Lesson lesson = lessonOptional.get();
        map.addAttribute("lesson", lesson);
        if (lesson.getLessonType() == LessonType.TEST){
            map.addAttribute("lessonType", "Test");
        } else {
            map.addAttribute("lessonType", "Video");
        }
        return "teacher/edit-lesson";
    }

    @GetMapping("/profile")
    public String profile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
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
        return "teacher/profile";
    }

    @GetMapping("/update/profile")
    public String updateProfile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
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
        return "teacher/updateProfile";
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
