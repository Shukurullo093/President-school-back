package com.example.president_school.entity;

import com.example.president_school.entity.enums.LessonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String title;

    @Column(length = 1024)
    private String description;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdDate;
}
