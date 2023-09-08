package com.example.president_school.entity;

import com.example.president_school.entity.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(length = 1024)
    private String description;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_video_source_id")
    private LessonSource lessonVideoSource;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_test_source_id")
    private LessonSource lessonTestSource;

    private String testAnswer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_task_source_id")
    private LessonSource lessonTaskSource;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @CreationTimestamp
    private Date createdDate;
}
