package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    private int id;
    private int order;

    private String question;
    private String questionImgUrl;

    private String answer1;
    private String answer1ImgUrl;
    private int answer1Id;

    private String answer2;
    private String answer2ImgUrl;
    private int answer2Id;

    private String answer3;
    private String answer3ImgUrl;
    private int answer3Id;
}
