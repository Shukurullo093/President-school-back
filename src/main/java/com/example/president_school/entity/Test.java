package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

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

    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "question_img_id")
    private TestImageSource questionImg;

    private String answer1;
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "answer_1_img_id")
    private TestImageSource answer1Img;

    private String answer2;
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "answer_2_img_id")
    private TestImageSource answer2Img;

    private String answer3;
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "answer_3_img_id")
    private TestImageSource answer3Img;
}
