package com.example.president_school.payload;

import com.example.president_school.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDto {
    private Long id;
    private String messageOwnerRole;
    private EmployeeDto assistant;
    private StudentDto student;
    private boolean[] star;
    private String message;
    private String messageImagePath;
    private String date;
    private UUID lessonId;
    private int lessonOrder;
    private int taskOrder;
    private String scrollId;

    public ChatDto(String messageOwnerRole, String date, String scrollId) {
        this.messageOwnerRole = messageOwnerRole;
        this.date = date;
        this.scrollId = scrollId;
    }
}
