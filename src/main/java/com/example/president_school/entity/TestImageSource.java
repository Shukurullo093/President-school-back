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
public class TestImageSource extends AbsFileInfoEntity {
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
}
