package com.example.president_school.entity;

import com.example.president_school.entity.templates.AbsFileInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageImage extends AbsFileInfoEntity {
}
