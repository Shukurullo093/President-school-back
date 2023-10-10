package com.example.president_school.controller;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Post;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.PostDto;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.repository.LessonRepository;
import com.example.president_school.repository.PostRepository;
import com.example.president_school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeApiController {
    private final StudentRepository studentRepository;
    private final EmployeeRepository employeeRepository;
    private final LessonRepository lessonRepository;
    private final PostRepository postRepository;

    @Value("${default.person.image.path}")
    private String defaultPersonImgPath;

    @GetMapping("")
    public String home(Model map){
        int studentSize = studentRepository.findAll().size();
        map.addAttribute("studentSize", studentSize > 1000 ? studentSize : 1575);
        int employeeSize = employeeRepository.findAll().size();
        map.addAttribute("employeeSize", employeeSize);
        int lessonSize = lessonRepository.findAll().size();
        map.addAttribute("lessonSize", lessonSize > 1000 ? lessonSize : 1805);
//        ********************
        List<Employee> employeeList = employeeRepository.findByRoleNot(Role.ADMIN);
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee employee : employeeList){
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setFullName(employee.getLastName() + " " + employee.getFirstName());
            employeeDto.setPhone(employee.getPhone());
            employeeDto.setRole(employee.getRole().toString());
            employeeDto.setScience(employee.getScience().toString());
            employeeDto.setImageHashId(employee.getImage() != null ? "/api/admin/rest/viewImage/" + employee.getImage().getHashId() : defaultPersonImgPath);
            employeeDtoList.add(employeeDto);
        }
        map.addAttribute("employeeList", employeeDtoList);
//        ********************
        Pageable sortedByCreatedDate = PageRequest.of(0, 6, Sort.by("createdAt").descending());
        Page<Post> postList = postRepository.findAll(sortedByCreatedDate);

        List<PostDto> postDtoList = new ArrayList<>();
        for(Post post : postList){
            PostDto postDto = new PostDto();
            postDto.setId(post.getId());
            postDto.setTitle(post.getTitle());
//            postDto.setType(post.getType().equals("News") ? "Yangilik" : "Qonun");
            postDto.setDate(post.getCreatedAt().toString());
            postDto.setDescription((post.getDescription().length() > 80) ? post.getDescription().substring(0, 80) + "..." : post.getDescription());
            postDto.setImagePath("/api/admin/rest/post/image/" + post.getHashId());
            postDtoList.add(postDto);
        }
        map.addAttribute("postList", postDtoList);
//        ********************
        Employee admin = employeeRepository.findByRole(Role.ADMIN);
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setPhone(admin.getPhone());
        employeeDto.setEmail(admin.getEmail());
        map.addAttribute("contact", employeeDto);

        return "home/index";
    }

    @GetMapping("/news/{id}")
    public String getNewsById(@PathVariable String id, Model map){
        Optional<Post> postOptional = postRepository.findById(Integer.valueOf(id));
        Post post = postOptional.get();
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setDate(post.getCreatedAt().toString().split(" ")[0]);
        postDto.setType(post.getType().equals("News") ? "Yangilik" : "Qonun");
        postDto.setImagePath("/api/admin/rest/post/image/" + post.getHashId());

        map.addAttribute("post", postDto);
//        ************************************************
        Pageable sortedByCreatedDate = PageRequest.of(0, 15, Sort.by("createdAt").descending());
        Page<Post> postList = postRepository.findAll(sortedByCreatedDate);
        List<PostDto> postDtoList = new ArrayList<>();
        for(Post post1 : postList){
            PostDto postDto1 = new PostDto();
            postDto1.setId(post1.getId());
            postDto1.setTitle(post1.getTitle());
            postDto1.setType(post1.getType().equals("News") ? "Yangilik" : "Qonun");
            postDto1.setDate(post1.getCreatedAt().toString());
            postDto1.setImagePath("/api/admin/rest/post/image/" + post1.getHashId());
            postDtoList.add(postDto1);
        }
        map.addAttribute("postList", postDtoList);
        return "home/news";
    }
}
