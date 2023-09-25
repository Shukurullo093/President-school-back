package com.example.president_school.entity;

import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.templates.AbsFileInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "student_assistant_chat")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends AbsFileInfoEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

//    @OneToOne
//    @JoinColumn(name = "image_id")
//    private ChatMessageImage image;

    private int taskOrder;

    @Enumerated(EnumType.STRING)
    private Role messageOwner;

    private String message;

    private boolean viewStatus;

//    @Column(nullable = false, updatable = false)
//    @CreationTimestamp
//    private Date createdDate;
}
