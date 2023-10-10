package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private Integer orderNumber;

    @Column(length = 1024)
    private String taskBody;

    @OneToOne
    @JoinColumn(name = "task_img_id")
    private TaskSource taskImg;

    private String answer;

    @Column(length = 1024)
    private String exampleBody;

    @OneToOne
    @JoinColumn(name = "example_img_id")
    private TaskSource exampleImg;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;
}
