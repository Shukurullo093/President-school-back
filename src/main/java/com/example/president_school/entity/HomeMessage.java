package com.example.president_school.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "home_message")
@Entity
public class HomeMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ownerName;

    private String ownerPhone;

    private String message;

    private boolean messageStatus;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdDate;

    public HomeMessage(String name, String phone, String message, boolean status) {
        this.ownerName = name;
        this.ownerPhone = phone;
        this.message = message;
        this.messageStatus = status;
    }
}
