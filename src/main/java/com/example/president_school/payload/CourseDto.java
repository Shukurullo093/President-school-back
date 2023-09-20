package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDto {
    private String science;
    private int lessonCount;
    private String image;
    private int grade;
    private String lessonPath;
    private boolean lock;

    public CourseDto(String science, String image, int grade, String lessonPath, boolean lock) {
        this.science = science;
        this.image = image;
        this.grade = grade;
        this.lessonPath = lessonPath;
        this.lock = lock;
    }
}
