package com.example.president_school.controller;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.*;
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
    private final StudentTaskStatusRepository studentTaskStatusRepository;
    private final TaskRepository taskRepository;
    private final TestRepository testRepository;
    private final StudentTestRepository studentTestRepository;

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

            List<Lesson> lessonList = lessonRepository.findAllByCourseOrderByOrderNumberAsc(course.get());
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
            }
            else {
                map.addAttribute("lesson1", null);
                map.addAttribute("lesson2", null);
                List<LessonDto> lessonDtoList = new ArrayList<>();
                for (int i = 0; i < lessonList.size(); i++) {
                    if (accessLessonRepository.existsByLessonAndStudent(lessonList.get(i), studentRepository.findByPhone("+998901234568").get()) || i <= 1) {
                        String lessonLink = lessonList.get(i).getOrderNumber() % 7 != 0 ? "/api/user/watch/" + lessonList.get(i).getId()
                                : "/api/user/test/" + lessonList.get(i).getId();

                        boolean star = true;
                        if (i >= 1 && i <= lessonList.size() - 2){
                            if (!accessLessonRepository.existsByLessonAndStudent(lessonList.get(i+1), studentRepository.findByPhone("+998901234568").get())){
                                star = false;
                            }
                        }

                        lessonDtoList.add(new LessonDto(lessonList.get(i).getId(), lessonList.get(i).getOrderNumber(),
                                lessonList.get(i).getTitle(), lessonList.get(i).getLessonType().toString().toLowerCase(), "true", lessonLink, star));
                    } else {
                        lessonDtoList.add(new LessonDto(lessonList.get(i).getId(), lessonList.get(i).getOrderNumber(),
                                lessonList.get(i).getTitle(), lessonList.get(i).getLessonType().toString().toLowerCase(), "false", "javascript:void(0);", false));
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
                lessonDto.setId(lesson.getId());
                lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + lesson.getHashId());
                lessonDto.setTitle(lesson.getTitle());
                lessonDto.setDescription(lesson.getDescription());
                if (lesson.getLessonType().equals(LessonType.DEMO) && !b){
                    lessonDto.setChat(false);
                } else { lessonDto.setChat(true); }
                map.addAttribute("lesson", lessonDto);
            }
            else {
                map.addAttribute("lesson", null);
            }
        }
//        ******************************************
//        final List<Chat> chatList = chatRepository
//                .findByStudentIdAndLessonIdAndTaskOrderOrderByCreatedAtAsc(1L, UUID.fromString(lessonId), 1);
//        List<ChatDto> chatDtoList = new ArrayList<>();
//        if (chatList.size() > 0){
//            chatDtoList.add(new ChatDto("date", generalService.getDateFormat(chatList.get(0).getCreatedAt(), "dd MMM, yyy"), generalService.getDateFormat(chatList.get(0).getCreatedAt(), "dd_MMM_yyy")));
//            String scrollId = null;
//            for(int i = 0; i < chatList.size(); i++){
//                if (i == 0){
//                    scrollId = generalService.getDateFormat(chatList.get(0).getCreatedAt(), "dd_MMM_yyy");
//                }
//                if (i > 0 && generalService.after(chatList.get(i-1).getCreatedAt(), chatList.get(i).getCreatedAt())){
//                    chatDtoList.add(new ChatDto("date",
//                            generalService.getDateFormat(chatList.get(i).getCreatedAt(), "dd MMM, yyy"),
//                            generalService.getDateFormat(chatList.get(i).getCreatedAt(), "dd_MMM_yyy")));
//                    scrollId = generalService.getDateFormat(chatList.get(i).getCreatedAt(), "dd_MMM_yyy");
//                }
//                if (i > 0 && !generalService.after(chatList.get(i-1).getCreatedAt(), chatList.get(i).getCreatedAt())){
//                    scrollId = generalService.getDateFormat(chatList.get(i-1).getCreatedAt(), "dd_MMM_yyy");
//                }
//                ChatDto chatDto = new ChatDto();
//                chatDto.setId(Long.valueOf(chatList.get(i).getId()));
//                String whoMsg = null;
//                switch (chatList.get(i).getMessageOwner()){
//                    case STUDENT -> whoMsg = "my-message";
//                    case ASSISTANT -> whoMsg = "other-message";
//                }
//                chatDto.setMessageOwnerRole(whoMsg);
//                chatDto.setMessage(chatList.get(i).getMessage());
//                final String s = chatList.get(i).getHashId() == null ? null : "/api/assistant/rest/message/image/" + chatList.get(i).getHashId();
//                chatDto.setMessageImagePath(s);
//                chatDto.setDate(generalService.getDateFormat(chatList.get(i).getCreatedAt(), "HH:mm"));
//                chatDto.setScrollId(scrollId);
//                chatDtoList.add(chatDto);
//            }
//            Collections.reverse(chatDtoList);
//        }

//        map.addAttribute("historyList", chatDtoList);

        return "student/watch-video";
    }

    @GetMapping("/{lesson}/task")
    public String task(@PathVariable UUID lesson, Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));
        final Optional<Student> byPhone = studentRepository.findByPhone("+998901234568");

        final Optional<Lesson> lessonOptional = lessonRepository.findById(lesson);
        List<TaskDto> taskDtoList = new ArrayList<>();

        if (lessonOptional.isPresent()){
            map.addAttribute("lesson", "/api/user/watch/" + lessonOptional.get().getId());
            String nextLessonPath;
            if (lessonOptional.get().getOrderNumber() % 6 == 0) {
                nextLessonPath = "javascript:void(0);";
            } else {
                final Optional<Lesson> byCourseIdAndOrderNumber = lessonRepository.findByCourseIdAndOrderNumber(lessonOptional.get().getCourse().getId(), lessonOptional.get().getOrderNumber() + 1);
                nextLessonPath = byCourseIdAndOrderNumber.map(value -> "/api/user/watch/" + value.getId()).orElse("javascript:void(0);");
            }
            map.addAttribute("nextLesson", nextLessonPath);

            final List<Task> byLessonIdOrderByOrderNumber = taskRepository.findByLessonIdOrderByOrderNumber(lessonOptional.get().getId());
            for (Task task  : byLessonIdOrderByOrderNumber){
                TaskDto taskDto = new TaskDto();
                taskDto.setId(task.getId());
                taskDto.setOrder(task.getOrderNumber());
                taskDto.setTaskBody(task.getTaskBody());
                taskDto.setTaskImg("/api/teacher/rest/view-task-image/" + task.getTaskImg().getHashId());
                taskDto.setExample(task.getExampleBody());
                taskDto.setExampleImg("/api/teacher/rest/view-task-image/" + task.getExampleImg().getHashId());
                final boolean b = studentTaskStatusRepository.existsByStudentAndLessonAndTaskOrder(byPhone.get(), lessonOptional.get(), task.getOrderNumber());
                taskDto.setTaskDo(b);
                taskDtoList.add(taskDto);
            }
        }
        map.addAttribute("taskList", taskDtoList);
        return "student/task";
    }

    @GetMapping("/test/{lessonId}")
    public String test(@PathVariable String lessonId, Model map){
        map.addAttribute("profile", generalService.getProfile("+998901234568"));

        final Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(lessonId));
        final Lesson lesson = lessonOptional.get();
        map.addAttribute("title", lesson.getTitle());
        map.addAttribute("description", lesson.getDescription());

        List<TestDto> testDtoList = new ArrayList<>();
        int testOrder = 1;
        for (int i = lesson.getOrderNumber() - 6; i <= lesson.getOrderNumber() - 1; i++){
            final Optional<Lesson> byCourseIdAndOrderNumber = lessonRepository.findByCourseIdAndOrderNumber(lesson.getCourse().getId(), i);
            final List<Test> allByLesson = testRepository.findAllByLesson(byCourseIdAndOrderNumber.get());
            if (allByLesson.size() >= 3){
                List<Integer> arr = new ArrayList<>();
                Random random = new Random();
                do {
                    int i1 = random.nextInt(0, allByLesson.size());
                    if (!arr.contains(i1)) arr.add(i1);
                } while (arr.size() != 3);

                for(int j = 0; j < 3; j++){
                    TestDto testDto = new TestDto();
                    testDto.setOrder(testOrder);
                    testOrder++;
                    testDto.setId(allByLesson.get(arr.get(j)).getId());
                    testDto.setQuestion(allByLesson.get(arr.get(j)).getQuestionTxt());
                    String questionImgPath = allByLesson.get(arr.get(j)).getQuestionImg() == null ? null
                            : "/api/teacher/rest/view-test-image/" + allByLesson.get(arr.get(j)).getQuestionImg().getHashId();
                    testDto.setQuestionImgUrl(questionImgPath);
                    List<Integer> ans = new ArrayList<>();
                    do {
                        int i1 = random.nextInt(1, 4);
                        if (!ans.contains(i1)) ans.add(i1);
                    } while (ans.size() != 3);

                    String answer1ImgPath = allByLesson.get(arr.get(j)).getAnswer1Img() == null ? null
                            : "/api/teacher/rest/view-test-image/" + allByLesson.get(arr.get(j)).getAnswer1Img().getHashId();
                    String answer2ImgPath = allByLesson.get(arr.get(j)).getAnswer2Img() == null ? null
                            : "/api/teacher/rest/view-test-image/" + allByLesson.get(arr.get(j)).getAnswer2Img().getHashId();
                    String answer3ImgPath = allByLesson.get(arr.get(j)).getAnswer3Img() == null ? null
                            : "/api/teacher/rest/view-test-image/" + allByLesson.get(arr.get(j)).getAnswer3Img().getHashId();


                    switch (ans.get(0)) {
                        case 1 -> {
                            testDto.setAnswer1(allByLesson.get(arr.get(j)).getAnswer1());
                            testDto.setAnswer1ImgUrl(answer1ImgPath);
                            testDto.setAnswer1Id(1);
                        }
                        case 2 -> {
                            testDto.setAnswer1(allByLesson.get(arr.get(j)).getAnswer2());
                            testDto.setAnswer1ImgUrl(answer2ImgPath);
                            testDto.setAnswer1Id(2);
                        }
                        case 3 -> {
                            testDto.setAnswer1(allByLesson.get(arr.get(j)).getAnswer3());
                            testDto.setAnswer1ImgUrl(answer3ImgPath);
                            testDto.setAnswer1Id(3);
                        }
                    }
                    switch (ans.get(1)) {
                        case 1 -> {
                            testDto.setAnswer2(allByLesson.get(arr.get(j)).getAnswer1());
                            testDto.setAnswer2ImgUrl(answer1ImgPath);
                            testDto.setAnswer2Id(1);
                        }
                        case 2 -> {
                            testDto.setAnswer2(allByLesson.get(arr.get(j)).getAnswer2());
                            testDto.setAnswer2ImgUrl(answer2ImgPath);
                            testDto.setAnswer2Id(2);
                        }
                        case 3 -> {
                            testDto.setAnswer2(allByLesson.get(arr.get(j)).getAnswer3());
                            testDto.setAnswer2ImgUrl(answer3ImgPath);
                            testDto.setAnswer2Id(3);
                        }
                    }
                    switch (ans.get(2)) {
                        case 1 -> {
                            testDto.setAnswer3(allByLesson.get(arr.get(j)).getAnswer1());
                            testDto.setAnswer3ImgUrl(answer1ImgPath);
                            testDto.setAnswer3Id(1);
                        }
                        case 2 -> {
                            testDto.setAnswer3(allByLesson.get(arr.get(j)).getAnswer2());
                            testDto.setAnswer3ImgUrl(answer2ImgPath);
                            testDto.setAnswer3Id(2);
                        }
                        case 3 -> {
                            testDto.setAnswer3(allByLesson.get(arr.get(j)).getAnswer3());
                            testDto.setAnswer3ImgUrl(answer3ImgPath);
                            testDto.setAnswer3Id(3);
                        }
                    }

                    testDtoList.add(testDto);
                }
                for(TestDto testDto : testDtoList){
                    final Optional<Student> studentOptional = studentRepository.findByPhone("+998901234568");
                    final Student student = studentOptional.get();
                    final Optional<Test> testOptional = testRepository.findById(testDto.getId());
                    final Test test = testOptional.get();
                    studentTestRepository.save(new StudentTest(student, test));
                }
            }
        }
        
        map.addAttribute("allByLesson", testDtoList);
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