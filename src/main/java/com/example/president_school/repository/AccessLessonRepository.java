package com.example.president_school.repository;

import com.example.president_school.entity.AccessLesson;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLessonRepository extends JpaRepository<AccessLesson, Integer> {
    boolean existsByLessonAndStudent(Lesson lesson, Student student);
}
