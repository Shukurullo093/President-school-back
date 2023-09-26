package com.example.president_school.controller;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.ChatDto;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.payload.LessonDto;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.*;
import com.example.president_school.service.GeneralService;
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
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantApiController {
    private final EmployeeRepository employeeRepository;
    private final ChatRepository chatRepository;
    private final StudentTaskStatusRepository studentTaskStatusRepository;
    private final GeneralService generalService;
    private final LessonRepository lessonRepository;
    private final VideoSourceRepository videoSourceRepository;
    private final TaskSourceRepository taskSourceRepository;

    @Value("${default.person.image.path}")
    private String defaultPersonImgPath;

    @GetMapping("/all/message")
    public String getAllMessage(Model map){
        final List<Chat> byViewStatus = chatRepository
                .findByLessonCourseScienceAndMessageOwnerAndViewStatusOrderByCreatedAtAsc(Science.MATH, Role.STUDENT, false);

        List<Chat> chatList = new ArrayList<>();
        byViewStatus.forEach(chat -> {
            boolean available = true;
            for (Chat chat1 : chatList) {
                if (chat1.getStudent().equals(chat.getStudent()) && chat1.getLesson().equals(chat.getLesson()) && chat1.getTaskOrder() == chat.getTaskOrder()){
                    chatList.set(chatList.indexOf(chat1), chat);
                    available = false;
                    break;
                }
            }
            if (available){
                chatList.add(chat);
            }
        });
        List<ChatDto> chatDtoList = new ArrayList<>();
        if (byViewStatus.size() > 0){
            for(Chat chat : chatList){
                ChatDto chatDto = new ChatDto();
                chatDto.setId(Long.valueOf(chat.getId()));
                Student student = chat.getStudent();
                String img = chat.getStudent().getImage() != null ? generalService.getProfile(chat.getStudent().getPhone()).getImageHashId()
                        : defaultPersonImgPath;
                chatDto.setStudent(new StudentDto(student.getId(), student.getFullName(), img));
                final List<Lesson> allByCourseOrderByCreatedDateAsc = lessonRepository.findAllByCourseOrderByCreatedDateAsc(chat.getLesson().getCourse());
                chatDto.setLessonId(chat.getLesson().getId());
                chatDto.setLessonOrder(allByCourseOrderByCreatedDateAsc.indexOf(chat.getLesson()) + 1);
                boolean[] star = new boolean[5];
                for(int i = 1; i <= 5; i++){
                    star[i-1] = studentTaskStatusRepository.existsByStudentAndLessonAndTaskOrder(student, chat.getLesson(), i);
                };
                chatDto.setStar(star);
                chatDto.setMessage(chat.getMessage());
                chatDto.setTaskOrder(chat.getTaskOrder());
                chatDtoList.add(chatDto);
            }
        }
        map.addAttribute("chatList", chatDtoList);

        return "assistant/assistant-dashboard";
    }

    @GetMapping("/message/{chatId}")
    public String getMessageByStudentAndLesson(Model map, @PathVariable Integer chatId){
        final Optional<Chat> chatOptional = chatRepository.findById(chatId);
        final Chat chat = chatOptional.get();

        final Student student = chat.getStudent();
        ChatDto chatDto1 = new ChatDto();
        String img = chat.getStudent().getImage() != null ? generalService.getProfile(chat.getStudent().getPhone()).getImageHashId()
                : defaultPersonImgPath;
        chatDto1.setStudent(new StudentDto(student.getId(), student.getFullName(), img));
        chatDto1.setLessonId(chat.getLesson().getId());
        final List<Lesson> allByCourseOrderByCreatedDateAsc = lessonRepository.findAllByCourseOrderByCreatedDateAsc(chat.getLesson().getCourse());
        chatDto1.setLessonOrder(allByCourseOrderByCreatedDateAsc.indexOf(chat.getLesson()) + 1);
        chatDto1.setTaskOrder(chat.getTaskOrder());
        boolean[] star = new boolean[5];
        for(int i = 1; i <= 5; i++){
            star[i-1] = studentTaskStatusRepository.existsByStudentAndLessonAndTaskOrder(student, chat.getLesson(), i);
        };
        chatDto1.setStar(star);
        map.addAttribute("student", chatDto1);
//        ================================
        final List<Chat> byStudentAndLessonCourseOrderByCreatedDateDesc = chatRepository.findByStudentAndLessonCourseOrderByCreatedAtDesc(chat.getStudent(), chat.getLesson().getCourse());
        List<ChatDto> chatDtoListHistory = new ArrayList<>();
        for(Chat chat1 : byStudentAndLessonCourseOrderByCreatedDateDesc){
            ChatDto chatDto = new ChatDto();
            chatDto.setId(Long.valueOf(chat1.getId()));
            chatDto.setMessageOwnerRole(chat1.getMessageOwner().name());
            chatDto.setMessage(chat1.getMessage());
            chatDto.setMessageImagePath(chat1.getHashId() == null ? null : "/api/assistant/rest/message/image/" + chat1.getHashId());
            chatDto.setDate(getDateFormat(chat1.getCreatedAt(), "HH:MM, dd-MM-yyyy"));
            chatDtoListHistory.add(chatDto);

//            chat1.setViewStatus(true);
//            chatRepository.save(chat1);
        }
        map.addAttribute("chatHistory", chatDtoListHistory);
        map.addAttribute("lastTask", byStudentAndLessonCourseOrderByCreatedDateDesc.get(byStudentAndLessonCourseOrderByCreatedDateDesc.size()-1).getId());
//        ===============================
        LessonDto lessonDto = new LessonDto();
        final Lesson lesson = chat.getLesson();
        lessonDto.setTitle(lesson.getTitle());
        final Optional<VideoSource> videoSource = videoSourceRepository.findByLessonId(lesson.getId());
        final VideoSource video = videoSource.get();
        lessonDto.setVideoLink("/api/teacher/rest/viewVideo/" + video.getHashId());
        final TaskSource taskSource = taskSourceRepository.findByLessonId(lesson.getId()).get();
        lessonDto.setTaskLink("/api/admin/rest/pdf/" + taskSource.getHashId());

        map.addAttribute("lessonInfo", lessonDto);

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

    private String getDate(Date date){
        return date.toString().substring(0, date.toString().indexOf(" "));
    }

    private String getDateFormat(Date date, String format) {
        DateFormat monthFormat = new SimpleDateFormat(format);
        return monthFormat.format(date);
    }
}