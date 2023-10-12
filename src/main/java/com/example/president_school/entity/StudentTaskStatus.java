package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private int taskOrder;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public StudentTaskStatus(Student student, Lesson lesson, int taskOrder) {
        this.student = student;
        this.lesson = lesson;
        this.taskOrder = taskOrder;
    }
}
