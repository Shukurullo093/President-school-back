package com.example.president_school.entity;

import com.example.president_school.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AccessCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date date;

    private Role role;

    public AccessCourse(Student student, Course course, Role role) {
        this.student = student;
        this.course = course;
        this.role = role;
    }
}
