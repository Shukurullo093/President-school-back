package com.example.president_school.controller;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.*;
import com.example.president_school.repository.*;
import com.example.president_school.service.GeneralService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final VideoSourceRepository videoSourceRepository;
    private final TaskSourceRepository taskSourceRepository;
    private final GeneralService generalService;
    private final PostRepository postRepository;
    private final HomeMessageRepository homeMessageRepository;

    @Value("${default.person.image.path}")
    private String defaultPersonImgPath;
    String path = String.valueOf(this.getClass().getProtectionDomain().getCodeSource().getLocation()).substring("file:/".length());

    @GetMapping("/dashboard")
    public String getDashboard(Model map){
//        System.out.println(path);
//        path = path.replace("%20", " ");
//        final int target = path.indexOf("target");
//        System.out.println(path.substring(0, target));

        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }
//        ********************************************
        final List<HomeMessage> messageList = homeMessageRepository.findByMessageStatus(true);
        List<MessageDto> messageDtoList = new ArrayList<>();
        messageList.forEach(homeMessage -> {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(homeMessage.getId());
            messageDto.setFullName(homeMessage.getOwnerName());
            messageDto.setPhone(homeMessage.getOwnerPhone());
            messageDto.setMessage(homeMessage.getMessage());
            messageDto.setStatus(homeMessage.isMessageStatus());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            messageDto.setDate(dateFormat.format(homeMessage.getCreatedDate()));
            messageDtoList.add(messageDto);
        });
        map.addAttribute("messageList", messageDtoList);
//        ********************************************

        final List<Student> studentList = studentRepository.findAll();
        map.addAttribute("totalSize", studentList.size());

        final List<Student> newStudents = new ArrayList<>();
        studentList.forEach(student -> {
            if (student.getCreatedDate().getMonth() == new Date().getMonth() && student.getCreatedDate().getYear() == new Date().getYear()){
                newStudents.add(student);
            }
        });
        map.addAttribute("newStudentList", newStudents);
        map.addAttribute("newStudentSize", newStudents.size());

        final List<Course> courseList = courseRepository.findAll();
        map.addAttribute("courseSize", courseList.size());
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

   @GetMapping("/all/student")
   public String getAllStudent(Model map){
       Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
       Employee employee = employeeOptional.get();
       if (employee.getImage() != null){
           map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
       }
       else {
           map.addAttribute("img", defaultPersonImgPath);
       }

       final List<Student> all = studentRepository.findAll();
       List<StudentDto> studentDtoList = new ArrayList<>();
        for(Student student : all){
            StudentDto studentDto = new StudentDto();
            studentDto.setFullName(student.getFullName());
            studentDto.setPhone(student.getPhone());
            studentDto.setGender(student.getGender().equals("male") ? "O'g'il" : "Qiz");
            studentDto.setGrade(String.valueOf(student.getGrade()));
            studentDto.setImagePath(student.getImage() != null ? generalService.getProfile(student.getPhone()).getImageHashId() : defaultPersonImgPath);
            DateFormat monthFormat;
            monthFormat = new SimpleDateFormat("yyyy/MM/dd");
            studentDto.setCreatedDate(monthFormat.format(student.getCreatedDate()));
            studentDtoList.add(studentDto);
        }
        map.addAttribute("studentList", studentDtoList);
       return "admin/all-student";
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
            if(employee.getImage() != null)
                employeeDto.setImageHashId(employee.getImage().getHashId());
            else employeeDto.setImageHashId(null);
        }

        map.addAttribute("employee", employeeDto);
        return "admin/employeeProfile";
    }

    @GetMapping("/courses")
    public String getCourses(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        List<CourseDtoList> courseDtoListList = new ArrayList<>();
        for (Science science : Science.values()){
            List<CourseDto> courseDtoList = new ArrayList<>();
            for (int i = 2; i < 5; i++){
                Optional<Course> course = courseRepository.findByScienceAndGrade(science, i);
                CourseDto courseDto = new CourseDto();
                courseDto.setScience(science.toString());
                if (course.isPresent()){
                    int i1 = lessonRepository.countAllByCourse(course.get());
                    courseDto.setLessonCount(i1);
                    if (i1 == 0) courseDto.setLessonPath("javascript:void(0);");
                    else courseDto.setLessonPath("/api/admin/all/lesson/" + science.name().toLowerCase() + "/" + i);
                } else {
                    courseDto.setLessonCount(0);
                    courseDto.setLessonPath("javascript:void(0);");
                }
                if (i == 2) courseDto.setImage("/images/second.png");
                if (i == 3) courseDto.setImage("/images/third.png");
                if (i == 4) courseDto.setImage("/images/f44.png");
                courseDtoList.add(courseDto);
            }
            courseDtoListList.add(new CourseDtoList(science.toString(), courseDtoList));
        }
        map.addAttribute("courseList", courseDtoListList);
        return "admin/courses";
    }

    @GetMapping("/all/lesson/{science}/{grade}")
    public String getAllLesson(Model map, @PathVariable String science, @PathVariable String grade){
        Optional<Employee> employeeOptional1 = employeeRepository.findByPhone("+998901234567");
        Employee employee12 = employeeOptional1.get();
        if (employee12.getImage() != null){
            map.addAttribute("img", employeeOptional1.get().getImage().getHashId());
        }
        else {
            map.addAttribute("img", null);
        }

        String science1 = grade + "-sinf " + Science.valueOf(science.toUpperCase()) + " darslari ro'yhati";
        map.addAttribute("science", science1);
        Course course = courseRepository.findByScienceAndGrade(Science.valueOf(science.toUpperCase()), Integer.parseInt(grade)).get();
        List<Lesson> all = lessonRepository.findAllByCourseOrderByCreatedDateAsc(course);
        List<LessonDto> lessonDtoList = new ArrayList<>();
        int i=1;
        for (Lesson lesson : all) {
            lessonDtoList.add(new LessonDto(lesson.getId(), i, lesson.getTitle(), lesson.getLessonType().toString(), "true"));
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

        Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(id));
        Lesson lesson = lessonOptional.get();
        Optional<Employee> employeeOptional = employeeRepository.findById(lesson.getCourse().getEmployee().getId());
        Employee employee = employeeOptional.get();
        map.addAttribute("lessonOwner", new EmployeeDto(employee.getId(),
                employee.getLastName() + ' ' + employee.getFirstName(),
                null, null, null, null,
                employee.getScience().toString(), null,
                "/api/admin/rest/viewImage/" + employee.getImage().getHashId(),
                employee.getRole().toString(), employee.getGrade()));

        LessonDto lessonDto = new LessonDto();

        lessonDto.setTitle(lesson.getTitle());
        lessonDto.setType(lesson.getLessonType().toString());
        lessonDto.setDescription(lesson.getDescription());

        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd-MM-yyyy");
        lessonDto.setCreatedDate(monthFormat.format(lesson.getCreatedDate()));
        final Optional<VideoSource> videoSource = videoSourceRepository.findByLessonId(UUID.fromString(id));
        final VideoSource video = videoSource.get();
        lessonDto.setLessonName(video.getName());
        lessonDto.setContentType(video.getContentType());
        long fileSize = video.getFileSize() / (1024 * 1024);
        lessonDto.setSize(Long.toString(fileSize));
        lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + video.getHashId());
        final TaskSource taskSource = taskSourceRepository.findByLessonId(UUID.fromString(id)).get();
        lessonDto.setTaskLink("/api/admin/rest/pdf/" + taskSource.getHashId());
        lessonDto.setTestLink("javascript:void(0);");

        map.addAttribute("lessonInfo", lessonDto);
        return "admin/about-lesson";
    }

    @GetMapping("/all/news")
    public String getAllNews(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        List<Post> postList = postRepository.findAll(Sort.by("createdAt").descending());
        List<PostDto> postDtoList = new ArrayList<>();
        for(Post post : postList){
            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setTitle(post.getTitle());
            postDto.setDescription((post.getDescription().length() > 80) ? post.getDescription().substring(0, 80) + "..." : post.getDescription());
            postDto.setType(post.getType());
            DateFormat monthFormat;
            monthFormat = new SimpleDateFormat("dd/MM/yyyy");
            postDto.setDate(monthFormat.format(post.getCreatedAt()));
            postDto.setImagePath("/api/admin/rest/post/image/" + post.getHashId());
            postDtoList.add(postDto);
        }
        map.addAttribute("postList", postDtoList);
        return "admin/all-posts";
    }

    @GetMapping("/add/news")
    public String createNews(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", defaultPersonImgPath);
        }
        return "admin/news";
    }

    @GetMapping("/about/news/{id}")
    public String aboutNews(@PathVariable Integer id, Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        final Optional<Post> postOptional = postRepository.findById(id);
        PostDto postDto = new PostDto();
        if (postOptional.isPresent()){
            final Post post = postOptional.get();
            postDto.setId(post.getId());
            postDto.setTitle(post.getTitle());
            postDto.setDescription(post.getDescription());
            postDto.setType(post.getType());
            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            postDto.setDate(dateFormat.format(post.getCreatedAt()));
            postDto.setImagePath("/api/admin/rest/post/image/" + post.getHashId());
        }
        map.addAttribute("post", postDto);
        return "admin/about-news";
    }

    @GetMapping("/edit/news/{id}")
    public String editNews(Model map, @PathVariable String id){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        final Optional<Post> postOp = postRepository.findById(Integer.valueOf(id));
        final Post post = postOp.get();
        map.addAttribute("post", post);
        return "admin/edit-news";
    }

    @GetMapping("/all/home/message")
    public String homeMessage(Model map){
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        Employee employee = employeeOptional.get();
        if (employee.getImage() != null){
            map.addAttribute("img", "/api/admin/rest/viewImage/" + employeeOptional.get().getImage().getHashId());
        } else {
            map.addAttribute("img", defaultPersonImgPath);
        }

        final List<HomeMessage> messageList = homeMessageRepository.findAll(Sort.by("createdDate").descending());
        List<MessageDto> messageDtoList = new ArrayList<>();
        messageList.forEach(homeMessage -> {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(homeMessage.getId());
            messageDto.setFullName(homeMessage.getOwnerName());
            messageDto.setPhone(homeMessage.getOwnerPhone());
            messageDto.setMessage(homeMessage.getMessage());
            messageDto.setStatus(homeMessage.isMessageStatus());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            messageDto.setDate(dateFormat.format(homeMessage.getCreatedDate()));
            messageDtoList.add(messageDto);

            homeMessage.setMessageStatus(false);
            homeMessageRepository.save(homeMessage);
        });
        map.addAttribute("messageList", messageDtoList);
        return "admin/home-message-list";
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

    private String getDateFormat(Date date) {
        DateFormat monthFormat;
        monthFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return monthFormat.format(date);
    }

    private String getDate(Date date){
        return date.toString().substring(0, date.toString().indexOf(" "));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    class CourseDtoList{
        private String science;
        private List<CourseDto> courseDtoList;
    }
}
