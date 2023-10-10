package com.example.president_school.entity;

import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
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
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private Date birthDate;

    private String gender;

    private String phone;

    private String password;

    private Date joiningDate;

    @Enumerated(value = EnumType.STRING)
    private Science science;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String grade;

    @OneToOne
    private PersonImage image;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdDate;

    public Employee(String email, String phone, String password, Role role, Date joiningDate) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.joiningDate = joiningDate;
    }
}
