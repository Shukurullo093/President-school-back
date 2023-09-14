package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private UUID id;
    private int orderNumber;
    private String title;
    private String type;

    private String size;
    private String description;
    private String lessonName;
    private String contentType;
    private String createdDate;
    private String testAnswer;

    private String videoLink;
    private String taskLink;
    private String testLink;
    private String lessonInfoLink;

    private String access;

    public LessonDto(UUID id, int orderNumber, String title, String type) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.type = type;
    }

    public LessonDto(UUID id, int orderNumber, String title, String description, String type, String access, String link) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.description = description;
        this.type = type;
        this.access = access;
        this.lessonInfoLink = link;
    }

    public LessonDto(UUID id, int orderNumber, String title, String type, String access) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.type = type;
        this.access = access;
    }
}