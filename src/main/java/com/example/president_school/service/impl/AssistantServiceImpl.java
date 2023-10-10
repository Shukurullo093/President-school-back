//package com.example.president_school.service.impl;
//
//import com.example.president_school.entity.Chat;
//import com.example.president_school.entity.Employee;
//import com.example.president_school.entity.Lesson;
//import com.example.president_school.entity.Student;
//import com.example.president_school.entity.enums.Role;
//import com.example.president_school.payload.ControllerResponse;
//import com.example.president_school.repository.ChatMessageImageRepository;
//import com.example.president_school.repository.ChatRepository;
//import com.example.president_school.repository.LessonRepository;
//import com.example.president_school.repository.StudentRepository;
//import com.example.president_school.service.AssistantService;
//import com.example.president_school.service.GeneralService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Service
//public class AssistantServiceImpl implements AssistantService {
//    private final ChatRepository chatRepository;
//    private final ChatMessageImageRepository chatMessageImageRepository;
//    private final GeneralService generalService;
//    private final StudentRepository studentRepository;
//    private final LessonRepository lessonRepository;
//
//    @Value("${upload.folder}")
//    private String uploadFolder;
//
//    public AssistantServiceImpl(ChatRepository chatRepository, ChatMessageImageRepository chatMessageImageRepository, GeneralService generalService, StudentRepository studentRepository, LessonRepository lessonRepository) {
//        this.chatRepository = chatRepository;
//        this.chatMessageImageRepository = chatMessageImageRepository;
//        this.generalService = generalService;
//        this.studentRepository = studentRepository;
//        this.lessonRepository = lessonRepository;
//    }
//
//    @Override
//    public Chat getMessageImage(String hashId) {
//        return chatRepository.findByHashId(hashId).get();
//    }
//
//    @Override
//    public ControllerResponse sendMsgToStudent(Employee employee, Long student, UUID lesson, Integer task, String text, MultipartFile image) {
////        final Optional<Chat> chatOptional = chatRepository.findById(task);
//        final Optional<Student> studentOptional = studentRepository.findById(student);
//        final Optional<Lesson> lessonOptional = lessonRepository.findById(lesson);
////        if (chatOptional.isPresent()){
////            final Chat chat = chatOptional.get();
//        Chat chat1 = new Chat();
//        chat1.setViewStatus(false);
//        chat1.setStudent(studentOptional.get());
//        chat1.setTaskOrder(task);
//        chat1.setLesson(lessonOptional.get());
//        chat1.setMessage(text);
//        chat1.setMessageOwner(Role.ASSISTANT);
//        chat1.setEmployee(employee);
//        if (!image.isEmpty()){
//                File uploadFolder = new File(String.format("%s/CHAT/",
//                        this.uploadFolder));
//                if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                    System.out.println("Created folders. for chat images");
//                }
//
//                chat1.setContentType(image.getContentType());
//                chat1.setName(image.getOriginalFilename());
//                chat1.setExtension(generalService.getExtension(image.getOriginalFilename()));
//                chat1.setFileSize(image.getSize());
//                chat1.setHashId(UUID.randomUUID().toString().substring(0, 10));
//                chat1.setUploadPath(String.format("CHAT/%s.%s",
//                        chat1.getHashId(),
//                        chat1.getExtension()));
//
//                uploadFolder = uploadFolder.getAbsoluteFile();
//                File file = new File(uploadFolder, String.format("%s.%s",
//                        chat1.getHashId(),
//                        chat1.getExtension()));
//                try {
//                    image.transferTo(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//
//        chatRepository.save(chat1);
//        return new ControllerResponse("", 200);
//    }
//
//    @Override
//    public String getMsg(Long student, UUID lesson, Integer task) {
//        final Optional<Chat> byId = chatRepository.findById(task);
//        final Optional<Student> studentOptional = studentRepository.findById(student);
//        final Optional<Lesson> lessonOptional = lessonRepository.findById(lesson);
//        final List<Chat> byStudentAndLessonCourseOrderByCreatedDateDesc = chatRepository
//                .findByStudentAndLessonAndTaskOrderOrderByCreatedAtDesc(studentOptional.get(), lessonOptional.get(), task);
//        StringBuilder s = new StringBuilder();
//        for(Chat chat1 : byStudentAndLessonCourseOrderByCreatedDateDesc) {
//            s.append("<li><div class='" + (chat1.getMessageOwner().equals(Role.ASSISTANT) ? "message-data text-right" : "message-data") + "'>");
//            s.append("<span class='message-data-time'>" + getDateFormat(chat1.getCreatedAt(), "HH:mm, dd-MM-yyyy") + "</span>");
//            if (chat1.getMessageOwner().equals(Role.ASSISTANT)) {
//                s.append("<button class='ml-1 rounded-circle' data-id='" + chat1.getId() + "' onclick='deleteMsg(this)'><i class='la la-trash p-2'></i></button>");
//            }
//            s.append("</div>");
//            s.append("<div class='" + (chat1.getMessageOwner().equals(Role.ASSISTANT) ? "message my-message" : "message other-message") + "'>");
//            if (chat1.getHashId() != null) {
//                s.append("<img src='/api/assistant/rest/message/image/" + (chat1.getHashId()) + "' class='img-fluid'><br>");
//            }
//            s.append("<span>" + chat1.getMessage() + "</span></div></li>");
//        }
//        return s.toString();
//    }
//
//    @Override
//    public ControllerResponse deleteMsg(Integer id) {
//        final Optional<Chat> byId = chatRepository.findById(id);
//        if (byId.isPresent()){
//            chatRepository.deleteById(id);
//        }
//        return new ControllerResponse("Xabar o'chirildi", 200);
//    }
//
//    private String getDateFormat(Date date, String format) {
//        DateFormat monthFormat = new SimpleDateFormat(format);
//        return monthFormat.format(date);
//    }
//}
