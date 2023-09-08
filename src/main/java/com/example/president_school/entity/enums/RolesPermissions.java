package com.example.president_school.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RolesPermissions {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    TEACHER_READ("teacher:read"),
    TEACHER_CREATE("teacher:create"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_DELETE("teacher:delete"),

    ASSISTANT_READ("assistant:read"),
    ASSISTANT_CREATE("assistant:create"),
    ASSISTANT_UPDATE("assistant:update"),
    ASSISTANT_DELETE("assistant:delete"),

    STUDENT_READ("student:read"),
    STUDENT_CREATE("student:create"),
    STUDENT_UPDATE("student:update"),
    STUDENT_DELETE("student:delete");

    @Getter
    private final String permissions;
}
