package com.example.president_school.entity.enums;

public enum Role {
    ADMIN,
    TEACHER,
    ASSISTANT,
    STUDENT;

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Admin";
            case TEACHER -> "O'qituvchi";
            case ASSISTANT -> "Assistant";
            case STUDENT -> "O'quvchi";
        };
    }
}
