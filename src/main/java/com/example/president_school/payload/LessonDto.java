package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private UUID id;
    private int orderNumber;
    private String title;
    private String type;
    private boolean completedStatus;

    private String size;
    private String description;
    private String lessonName;
    private String contentType;
    private String createdDate;
    private String testAnswer;

    private String videoLink;
    private List<TaskDto> taskDtoList;
    private List<TestDto> testDtoList;
    private String lessonInfoLink;

    private String access;
    private boolean star;
    private boolean chat;
    private int viewStatus;

    public LessonDto(UUID id, int orderNumber, String title, String type, String access, String link, boolean star) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.type = type;
        this.access = access;
        this.lessonInfoLink = link;
        this.star = star;
    }

    public LessonDto(UUID id, int orderNumber, String title, String description, String type, String access, String link, boolean completedStatus) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.description = description;
        this.type = type;
        this.access = access;
        this.lessonInfoLink = link;
        this.completedStatus = completedStatus;
    }

    public LessonDto(UUID id, int orderNumber, String title, String type, String access) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.type = type;
        this.access = access;
    }
}
