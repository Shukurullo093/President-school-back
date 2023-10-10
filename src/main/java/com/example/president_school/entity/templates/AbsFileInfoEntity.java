package com.example.president_school.entity.templates;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbsFileInfoEntity {
    private String name;

    private String extension;

    private Long fileSize;

    @Column(unique = true)
    private String hashId;

    private String contentType;

    private String uploadPath;

//    @Column(nullable = false, updatable = false)
//    @CreationTimestamp
//    private Date createdAt;
}
