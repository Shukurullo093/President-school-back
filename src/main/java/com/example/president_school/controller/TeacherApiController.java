package com.example.president_school.controller;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.LessonDto;
import com.example.president_school.payload.TaskDto;
import com.example.president_school.payload.TestDto;
import com.example.president_school.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherApiController {
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final TaskSourceRepository taskSourceRepository;


    @Value("${default.person.image.path}")
    private String defaultPersonImgPath;

    @GetMapping("/dashboard")
    public String getDashboard(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }
        map.addAttribute("grade", employeeOptional.get().getGrade());
        if (employeeOptional.get().getGrade().equals("Hammasi")){
            List<Lesson> lessonList2 = lessonRepository.findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(2, employeeOptional.get());
            map.addAttribute("grade2Count", lessonList2.size());

            List<Lesson> lessonList3 = lessonRepository.findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(3, employeeOptional.get());
            map.addAttribute("grade3Count", lessonList3.size());

            List<Lesson> lessonList4 = lessonRepository.findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(4, employeeOptional.get());
            map.addAttribute("grade4Count", lessonList4.size());
        } else {
            List<Lesson> lessonList2 = lessonRepository
                    .findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(Integer.parseInt(employeeOptional.get().getGrade()), employeeOptional.get());
            map.addAttribute("grade" + employeeOptional.get().getGrade() + "Count", lessonList2.size());
        }

        return "teacher/teacher-dashboard";
    }

    @GetMapping("/add/lesson/{grade}")
    public String addLesson(@PathVariable String grade, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        List<Lesson> lessonListByGrade =
                lessonRepository.findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(Integer.valueOf(grade), employeeOptional.get());
        if (lessonListByGrade.size() % 7 == 0){
            map.addAttribute("lessonCount", lessonListByGrade.size() + 1);
        } else {
            map.addAttribute("lessonCount", lessonListByGrade.size() / 7 + lessonListByGrade.size() % 7);
        }
        map.addAttribute("science", employeeOptional.get().getScience().toString());
        map.addAttribute("grade", grade);
        if (lessonListByGrade.size() + 1 <= 2){
            map.addAttribute("lessonType", "Demo");
        } else {
            map.addAttribute("lessonType", "Video");
        }

        return "teacher/add-lesson";
    }

    @GetMapping("/add/test/{lessonId}")
    public String addTest(@PathVariable String lessonId, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        if (employeeOptional.get().getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        List<Test> testList = testRepository.findAllByLesson(lessonRepository.findById(UUID.fromString(lessonId)).get());
        map.addAttribute("title", lessonRepository.findById(UUID.fromString(lessonId)).get().getTitle());
        map.addAttribute("lessonId", lessonId);

        List<TestDto> testDtoList = new ArrayList<>();
        int i = 1;
        for(Test test : testList){
            TestDto testDto = new TestDto();
            testDto.setId(test.getId());
            testDto.setOrder(i);
            testDto.setQuestion(test.getQuestionTxt());
            testDto.setAnswer1(test.getAnswer1());
            testDto.setAnswer2(test.getAnswer2());
            testDto.setAnswer3(test.getAnswer3());
            if (test.getQuestionImg() != null){
                testDto.setQuestionImgUrl("/api/teacher/rest/viewImage/" + test.getQuestionImg().getHashId());
            }
            if (test.getAnswer1Img() != null){
                testDto.setAnswer1ImgUrl("/api/teacher/rest/viewImage/" + test.getAnswer1Img().getHashId());
            }
            if (test.getAnswer2Img() != null){
                testDto.setAnswer2ImgUrl("/api/teacher/rest/viewImage/" + test.getAnswer2Img().getHashId());
            }
            if (test.getAnswer3Img() != null){
                testDto.setAnswer3ImgUrl("/api/teacher/rest/viewImage/" + test.getAnswer3Img().getHashId());
            }
            testDtoList.add(testDto);
            i++;
        }
        map.addAttribute("testList", testDtoList);
        return "teacher/add-test";
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

        List<Lesson> lessonList = lessonRepository.findAllByCourseEmployeeOrderByOrderNumberAsc(employeeOptional.get());
        List<LessonDto> lessonDtoList2 = new ArrayList<>();
        List<LessonDto> lessonDtoList3 = new ArrayList<>();
        List<LessonDto> lessonDtoList4 = new ArrayList<>();

        int i2=1, i3=1, i4=1;
        for (Lesson lesson : lessonList) {
            String lessonPath = !lesson.getLessonType().equals(LessonType.TEST) ? "/api/teacher/lesson/info/" + lesson.getId() :
                    "/api/teacher/add/test/" + lesson.getId();
            if (lesson.getCourse().getGrade() == 2){
                lessonDtoList2.add(new LessonDto(lesson.getId(), i2, lesson.getTitle(),
                        lesson.getDescription(), lesson.getLessonType().toString(), "true", lessonPath));
                i2++;
            }
            else if (lesson.getCourse().getGrade() == 3){
                lessonDtoList3.add(new LessonDto(lesson.getId(), i3, lesson.getTitle(),
                        lesson.getDescription(), lesson.getLessonType().toString(), "true", lessonPath));
                i3++;
            }
            else {
                lessonDtoList3.add(new LessonDto(lesson.getId(), i4, lesson.getTitle(),
                        lesson.getDescription(), lesson.getLessonType().toString(), "true", lessonPath));
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

        List<Lesson> lessonList =
                lessonRepository.findAllByCourseGradeAndCourseEmployeeOrderByOrderNumberAsc(Integer.valueOf(grade), employeeOptional.get());
        List<LessonDto> lessonDtoList = new ArrayList<>();

        int i = 1;
        for (Lesson lesson : lessonList) {
            String lessonPath = !lesson.getLessonType().equals(LessonType.TEST) ? "/api/teacher/lesson/info/" + lesson.getId() :
                    "/api/teacher/add/test/" + lesson.getId();
            lessonDtoList.add(new LessonDto(lesson.getId(), lesson.getOrderNumber(), lesson.getTitle(),
                    lesson.getDescription(), lesson.getLessonType().name(), "true", lessonPath));
            i++;
        }
        map.addAttribute("gradeStatus", grade);
        map.addAttribute("grade" + grade, lessonDtoList);

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

        Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(id));
        Lesson lesson = lessonOptional.get();

        LessonDto lessonDto = new LessonDto();

        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setType(lesson.getLessonType().toString());
        lessonDto.setDescription(lesson.getDescription());

        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd-MM-yyyy");
        lessonDto.setCreatedDate(monthFormat.format(lesson.getCreatedDate()));
        lessonDto.setLessonName(lesson.getName());
        lessonDto.setContentType(lesson.getContentType());
        long fileSize = lesson.getFileSize() / (1024 * 1024);
        lessonDto.setSize(Long.toString(fileSize));
        lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + lesson.getHashId());
//  lesson tasks
        final List<Task> byLessonIdOrderByOrderNumber = taskRepository.findByLessonIdOrderByOrderNumber(lesson.getId());
        List<TaskDto> taskDtoList = new ArrayList<>();
        for (Task task  : byLessonIdOrderByOrderNumber){
            TaskDto taskDto = new TaskDto();
            taskDto.setOrder(task.getOrderNumber());
            taskDto.setTaskBody(task.getTaskBody());
            taskDto.setTaskImg("/api/teacher/rest/view-task-image/" + task.getTaskImg().getHashId());
            taskDto.setAnswer(task.getAnswer());
            taskDto.setExample(task.getExampleBody());
            taskDto.setExampleImg("/api/teacher/rest/view-task-image/" + task.getExampleImg().getHashId());
            taskDtoList.add(taskDto);
        }
        lessonDto.setTaskDtoList(taskDtoList);
//  lesson tests
        final List<Test> testList = testRepository.findAllByLesson(lesson);
        List<TestDto> testDtoList = new ArrayList<>();
        for(Test test : testList){
            TestDto testDto = new TestDto();
            testDto.setQuestion(test.getQuestionTxt());
            testDto.setQuestionImgUrl("/api/teacher/rest/view-test-image/" + test.getQuestionImg().getHashId());
            testDto.setAnswer1(test.getAnswer1());
            testDto.setAnswer1ImgUrl("/api/teacher/rest/view-test-image/" + test.getQuestionImg().getHashId());
            testDto.setAnswer2(test.getAnswer2());
            testDto.setAnswer2ImgUrl("/api/teacher/rest/view-test-image/" + test.getQuestionImg().getHashId());
            testDto.setAnswer3(test.getAnswer3());
            testDto.setAnswer3ImgUrl("/api/teacher/rest/view-test-image/" + test.getQuestionImg().getHashId());
            testDtoList.add(testDto);
        }
        lessonDto.setTestDtoList(testDtoList);
        map.addAttribute("lessonInfo", lessonDto);
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

        Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(id));
        Lesson lesson = lessonOptional.get();
        map.addAttribute("lesson", lesson);
        return "teacher/edit-lesson";
    }

    @GetMapping("/profile")
    public String profile(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", defaultPersonImgPath);
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
        if (employee.getImage() == null){
            employeeDto.setImageHashId(defaultPersonImgPath);}
        else {
        employeeDto.setImageHashId("/api/admin/rest/viewImage/" + employee.getImage().getHashId());}

        map.addAttribute("employee", employeeDto);
        return "teacher/profile";
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
