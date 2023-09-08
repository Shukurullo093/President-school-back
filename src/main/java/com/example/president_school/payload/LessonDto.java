package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Integer id;
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

    public LessonDto(Integer id, int orderNumber, String title, String type) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.title = title;
        this.type = type;
    }
}
