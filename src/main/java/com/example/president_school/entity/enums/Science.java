package com.example.president_school.entity.enums;

public enum Science {
    MATH,
    ENGLISHLANGUAGE,
    CRITICALTHINKING,
    PROBLEMSOLVING;


    @Override
    public String toString() {
        return switch (this) {
            case MATH -> "Matematika";
            case ENGLISHLANGUAGE -> "Ingliz tili";
            case CRITICALTHINKING -> "Tanqidiy fikrlash";
            case PROBLEMSOLVING -> "Muammoni yechish";
        };
    }
}
