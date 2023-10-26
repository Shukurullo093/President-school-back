package com.example.president_school.entity;

import com.example.president_school.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phone;

    private int grade;

    private String password;

    private String gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Role entryStatus;

    private int iq;

    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "person_image_id")
    private PersonImage image;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public Student(String fullName, String phone, int grade, String password, String gender, PersonImage image) {
        this.fullName = fullName;
        this.phone = phone;
        this.grade = grade;
        this.password = password;
        this.gender = gender;
        this.image = image;
    }
}
