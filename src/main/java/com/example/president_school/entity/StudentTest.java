package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "student_test")
public class StudentTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private int score;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public StudentTest(Student student, Lesson lesson, int score) {
        this.student = student;
        this.lesson = lesson;
        this.score = score;
    }
}
