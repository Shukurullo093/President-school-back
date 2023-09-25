package com.example.president_school.service.impl;

import com.example.president_school.entity.Chat;
import com.example.president_school.entity.ChatMessageImage;
import com.example.president_school.entity.Employee;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.ChatMessageImageRepository;
import com.example.president_school.repository.ChatRepository;
import com.example.president_school.service.AssistantService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class AssistantServiceImpl implements AssistantService {
    private ChatRepository chatRepository;
    private ChatMessageImageRepository chatMessageImageRepository;

    @Override
    public Chat getMessageImage(String hashId) {
        return chatRepository.findByHashId(hashId).get();
    }

    @Override
    public ControllerResponse sendMsgToStudent(Employee employee, Integer task, String text, MultipartFile photo) {
        final Optional<Chat> chatOptional = chatRepository.findById(task);
        if (chatOptional.isPresent()){
            final Chat chat = chatOptional.get();
            Chat chat1 = new Chat();
            chat1.setViewStatus(false);
            chat1.setStudent(chat.getStudent());
            chat1.setTaskOrder(chat.getTaskOrder());
            chat1.setLesson(chat.getLesson());
            chat1.setMessage(text);
            chat1.setMessageOwner(Role.ASSISTANT);
            chat1.setEmployee(employee);
            if (!photo.isEmpty()){

            }
        }
        return null;
    }
}
