package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private String questionTxt;
    @OneToOne
    @JoinColumn(name = "question_img_id")
    private TestImageSource questionImg;

    private String answer1;
    @OneToOne
    @JoinColumn(name = "answer_1_img_id")
    private TestImageSource answer1Img;

    private String answer2;
    @OneToOne
    @JoinColumn(name = "answer_2_img_id")
    private TestImageSource answer2Img;

    private String answer3;
    @OneToOne
    @JoinColumn(name = "answer_3_img_id")
    private TestImageSource answer3Img;
}
