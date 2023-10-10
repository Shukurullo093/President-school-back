package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private int order;
    private String taskBody;
    private String taskImg;
    private String answer;
    private String example;
    private String exampleImg;
}
