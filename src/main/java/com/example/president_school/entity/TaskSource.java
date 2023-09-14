package com.example.president_school.entity;

import com.example.president_school.entity.templates.AbsFileInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSource extends AbsFileInfoEntity {
    @OneToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
