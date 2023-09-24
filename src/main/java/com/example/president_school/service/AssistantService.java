package com.example.president_school.service;

import com.example.president_school.entity.ChatMessageImage;
import org.springframework.stereotype.Service;

@Service
public interface AssistantService {
    ChatMessageImage getMessageImage(String hashId);
}
