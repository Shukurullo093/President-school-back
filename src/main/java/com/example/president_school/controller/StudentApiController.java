package com.example.president_school.controller;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.CourseDto;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.LessonDto;
import com.example.president_school.payload.TestDto;
import com.example.president_school.repository.*;
import com.example.president_school.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class StudentApiController {
    private final StudentRepository studentRepository;
    private final GeneralService generalService;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final AccessCourseRepository accessCourseRepository;
    private final AccessLessonRepository accessLessonRepository;
    private final VideoSourceRepository videoSourceRepository;
    private final TaskSourceRepository taskSourceRepository;
    private final TestRepository testRepository;

    @GetMapping("/register")
    public String register(){
        return "student/register";
    }

    @GetMapping("/login")
    public String login(){
        return "student/login";
    }

    @GetMapping("/statistic")
    public String statistic(Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        return "student/home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        map.addAttribute("grade", generalService.getProfile("+998901234568").getGrade());
        final List<Course> courseList = courseRepository.findAllByGrade(Integer.valueOf(generalService.getProfile("+998901234568").getGrade()));
        String imgPath = null;
        List<CourseDto> courseDtoList = new ArrayList<>();
        for (Course course : courseList) {
            switch (course.getScience()){
                case MATH -> imgPath = "/user/images/BRO-Vector-iStock.jpg";
                case ENGLISHLANGUAGE -> imgPath = "/user/images/How-to-Learn-English-Speaking-at-Home-960x540.jpg";
                case CRITICALTHINKING -> imgPath = "/user/images/why-is-critical-thinking-important.jpg";
                case PROBLEMSOLVING -> imgPath = "/user/images/problem-solving-concept-technique.jpg";
            }
            boolean lock = accessCourseRepository.existsByCourseAndStudent(course, studentRepository.findByPhone("+998901234568").get());
            courseDtoList.add(new CourseDto(course.getScience().toString(),
                    imgPath,
                    course.getGrade(),
                    "/api/user/demo/" + course.getScience().name().toLowerCase() + "/" + course.getGrade(),
                    lock));
        }
        map.addAttribute("courseList", courseDtoList);
        return "student/dashboard";
    }

    @GetMapping("/my/course")
    public String myCourses(Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        final List<AccessCourse> allByStudent = accessCourseRepository.findAllByStudent(studentRepository.findByPhone("+998901234568").get());
        if (allByStudent.isEmpty()){
            map.addAttribute("accessCourse", null);
        }else {
            List<CourseDto> accessCourseDtoList = new ArrayList<>();
            String imgPath = null;
            for (AccessCourse accessCourse : allByStudent) {
                switch (accessCourse.getCourse().getScience()){
                    case MATH -> imgPath = "/user/images/BRO-Vector-iStock.jpg";
                    case ENGLISHLANGUAGE -> imgPath = "/user/images/How-to-Learn-English-Speaking-at-Home-960x540.jpg";
                    case CRITICALTHINKING -> imgPath = "/user/images/why-is-critical-thinking-important.jpg";
                    case PROBLEMSOLVING -> imgPath = "/user/images/problem-solving-concept-technique.jpg";
                }
                accessCourseDtoList.add(new CourseDto(accessCourse.getCourse().getScience().toString(),
                        imgPath,
                        accessCourse.getCourse().getGrade(),
                        "/api/user/demo/" + accessCourse.getCourse().getScience().name().toLowerCase() + "/" + accessCourse.getCourse().getGrade(),
                        true));
            }
            map.addAttribute("accessCourse", accessCourseDtoList);
        }
        return "student/myCourses";
    }

    @GetMapping("/demo/{science}/{grade}")
    public String playlist(@PathVariable String science, @PathVariable String grade, Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));

        Optional<Course> course = courseRepository.findByScienceAndGrade(Science.valueOf(science.toUpperCase()),
                Integer.parseInt(grade));
        if (course.isPresent()) {
            boolean b = accessCourseRepository.existsByCourseAndStudent(course.get(), studentRepository.findByPhone("+998901234568").get());
            String imgPath = null;
            switch (course.get().getScience()) {
                case MATH -> imgPath = "/user/images/BRO-Vector-iStock.jpg";
                case ENGLISHLANGUAGE -> imgPath = "/user/images/How-to-Learn-English-Speaking-at-Home-960x540.jpg";
                case CRITICALTHINKING -> imgPath = "/user/images/why-is-critical-thinking-important.jpg";
                case PROBLEMSOLVING -> imgPath = "/user/images/problem-solving-concept-technique.jpg";
            }
            map.addAttribute("scienceImg", imgPath);
            List<Lesson> lessonList = lessonRepository.findAllByCourseOrderByCreatedDateAsc(course.get());
            map.addAttribute("courseId", course.get().getId());
            map.addAttribute("science", Science.valueOf(science.toUpperCase()).toString());
            map.addAttribute("lessonSize", lessonList.size());
            map.addAttribute("tasksSize", (lessonList.size() - lessonList.size() / 7) * 5);
            map.addAttribute("testSize", lessonList.size() / 7);
            if (!b) {
                if (lessonList.size() >= 1) {
                    map.addAttribute("lesson1", new LessonDto(lessonList.get(0).getId(), 1, lessonList.get(0).getTitle(), lessonList.get(0).getLessonType().toString(), "true"));
                } else {
                    map.addAttribute("lesson1", null);
                }
                if (lessonList.size() >= 2) {
                    map.addAttribute("lesson2", new LessonDto(lessonList.get(1).getId(), 2, lessonList.get(1).getTitle(), lessonList.get(1).getLessonType().toString(), "true"));
                } else {
                    map.addAttribute("lesson2", null);
                }
            } else {
                map.addAttribute("lesson1", null);
                map.addAttribute("lesson2", null);
//                final List<Lesson> lessonList = lessonRepository.findAllByCourseOrderByCreatedDateAsc(course.get());
//                map.addAttribute("courseId", course.get().getId());
//                map.addAttribute("science", Science.valueOf(science.toUpperCase()).toString());
//                map.addAttribute("lessonSize", lessonList.size());
//                map.addAttribute("tasksSize", lessonList.size() * 5);
//                map.addAttribute("testSize", lessonList.size() / 7);
                List<LessonDto> lessonDtoList = new ArrayList<>();
                for (int i = 0; i < lessonList.size(); i++) {
                    if (accessLessonRepository.existsByLessonAndStudent(lessonList.get(i), studentRepository.findByPhone("+998901234568").get()) || i <= 1) {
                        lessonDtoList.add(new LessonDto(lessonList.get(i).getId(), i + 1,
                                lessonList.get(i).getTitle(), lessonList.get(i).getLessonType().toString().toLowerCase(), "true"));
                    } else {
                        lessonDtoList.add(new LessonDto(lessonList.get(i).getId(), i + 1,
                                lessonList.get(i).getTitle(), lessonList.get(i).getLessonType().toString().toLowerCase(), "false"));
                    }
                }
                map.addAttribute("lessonList", lessonDtoList);
            }
        }
        return "student/demo";
    }

    @GetMapping("/watch/{lessonId}")
    public String watch(@PathVariable String lessonId, Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(lessonId));
        if (lessonOptional.isPresent()){
            Lesson lesson = lessonOptional.get();
            boolean b = accessCourseRepository.existsByCourseAndStudent(lesson.getCourse(),
                    studentRepository.findByPhone("+998901234568").get());
            if (b || lesson.getLessonType().equals(LessonType.DEMO)){
                LessonDto lessonDto =new LessonDto();
                lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + videoSourceRepository.findByLessonId(lesson.getId()).get().getHashId());
                lessonDto.setTitle(lesson.getTitle());
                lessonDto.setDescription(lesson.getDescription());
                lessonDto.setTaskLink("/api/admin/rest/pdf/" + taskSourceRepository.findByLessonId(lesson.getId()).get().getHashId());
                map.addAttribute("lesson", lessonDto);
            }
            else {
                map.addAttribute("lesson", null);
            }
        }
        return "student/watch-video";
    }

    @GetMapping("/test/{lessonId}")
    public String test(@PathVariable String lessonId, Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));

        final Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(lessonId));
        final Lesson lesson = lessonOptional.get();
        map.addAttribute("title", lesson.getTitle());
        map.addAttribute("description", lesson.getDescription());

        final List<Test> testList = testRepository.findAllByLesson(lesson);
        List<Integer> arr = new ArrayList<>();
        Random random = new Random();
        do {
            int i1 = random.nextInt(0, testList.size());
            if (!arr.contains(i1)) arr.add(i1);
        } while (arr.size() != testList.size());

        List<TestDto> testDtoList = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            TestDto testDto = new TestDto();
            testDto.setOrder(i+1);
            testDto.setId(testList.get(arr.get(i)).getId());
            testDto.setQuestion(testList.get(arr.get(i)).getQuestionTxt());
            String questionImgPath = testList.get(arr.get(i)).getQuestionImg() == null ? null : "/api/teacher/rest/viewImage/" + testList.get(arr.get(i)).getQuestionImg().getHashId();
            testDto.setQuestionImgUrl(questionImgPath);
            List<Integer> ans = new ArrayList<>();
            do {
                int i1 = random.nextInt(1, 4);
                if (!ans.contains(i1)) ans.add(i1);
            } while (ans.size() != 3);

            String answer1ImgPath = testList.get(arr.get(i)).getAnswer1Img() == null ? null : "/api/teacher/rest/viewImage/" + testList.get(arr.get(i)).getAnswer1Img().getHashId();
            String answer2ImgPath = testList.get(arr.get(i)).getAnswer2Img() == null ? null : "/api/teacher/rest/viewImage/" + testList.get(arr.get(i)).getAnswer2Img().getHashId();
            String answer3ImgPath = testList.get(arr.get(i)).getAnswer3Img() == null ? null : "/api/teacher/rest/viewImage/" + testList.get(arr.get(i)).getAnswer3Img().getHashId();


            switch (ans.get(0)){
                case 1 : {
                    testDto.setAnswer1(testList.get(arr.get(i)).getAnswer1());
                    testDto.setAnswer1ImgUrl(answer1ImgPath);
                    testDto.setAnswer1Id(1);
                    break;
                }
                case 2 : {
                    testDto.setAnswer1(testList.get(arr.get(i)).getAnswer2());
                    testDto.setAnswer1ImgUrl(answer2ImgPath);
                    testDto.setAnswer1Id(2);
                    break;
                }
                case 3 :{
                    testDto.setAnswer1(testList.get(arr.get(i)).getAnswer3());
                    testDto.setAnswer1ImgUrl(answer3ImgPath);
                    testDto.setAnswer1Id(3);
                    break;
                }
            }
            switch (ans.get(1)){
                case 1 : {
                    testDto.setAnswer2(testList.get(arr.get(i)).getAnswer1());
                    testDto.setAnswer2ImgUrl(answer1ImgPath);
                    testDto.setAnswer2Id(1);
                    break;
                }
                case 2 : {
                    testDto.setAnswer2(testList.get(arr.get(i)).getAnswer2());
                    testDto.setAnswer2ImgUrl(answer2ImgPath);
                    testDto.setAnswer2Id(2);
                    break;
                }
                case 3 :{
                    testDto.setAnswer2(testList.get(arr.get(i)).getAnswer3());
                    testDto.setAnswer2ImgUrl(answer3ImgPath);
                    testDto.setAnswer2Id(3);
                    break;
                }
            }
            switch (ans.get(2)){
                case 1 : {
                    testDto.setAnswer3(testList.get(arr.get(i)).getAnswer1());
                    testDto.setAnswer3ImgUrl(answer1ImgPath);
                    testDto.setAnswer3Id(1);
                    break;
                }
                case 2 : {
                    testDto.setAnswer3(testList.get(arr.get(i)).getAnswer2());
                    testDto.setAnswer3ImgUrl(answer2ImgPath);
                    testDto.setAnswer3Id(2);
                    break;
                }
                case 3 :{
                    testDto.setAnswer3(testList.get(arr.get(i)).getAnswer3());
                    testDto.setAnswer3ImgUrl(answer3ImgPath);
                    testDto.setAnswer3Id(3);
                    break;
                }
            }

            testDtoList.add(testDto);
        }
        map.addAttribute("testList", testDtoList);
        return "student/test";
    }

    @GetMapping("/update/profile")
    public String updateProfile(Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        Student student = studentRepository.findByPhone("+998901234568").get();
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFullName(student.getFullName());
        employeeDto.setPhone(student.getPhone());
        employeeDto.setGrade(String.valueOf(student.getGrade()));
        employeeDto.setGender(student.getGender());
        map.addAttribute("student", employeeDto);
        return "student/update";
    }
}