package com.example.president_school.service.impl;

import com.example.president_school.entity.ChatMessageImage;
import com.example.president_school.repository.ChatMessageImageRepository;
import com.example.president_school.repository.ChatRepository;
import com.example.president_school.service.AssistantService;
import org.springframework.stereotype.Service;

@Service
public class AssistantServiceImpl implements AssistantService {
    private ChatRepository chatRepository;
    private ChatMessageImageRepository chatMessageImageRepository;

    @Override
    public ChatMessageImage getMessageImage(String hashId) {
        return chatMessageImageRepository.findByHashId(hashId).get();
    }
}
