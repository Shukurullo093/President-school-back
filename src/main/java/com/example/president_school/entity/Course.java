package com.example.president_school.entity;

import com.example.president_school.entity.enums.Science;
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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Science science;

    private Integer grade;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public Course(Science science, Integer grade, Employee employee) {
        this.science = science;
        this.grade = grade;
        this.employee = employee;
    }
}
