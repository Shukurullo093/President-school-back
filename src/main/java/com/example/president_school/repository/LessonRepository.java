package com.example.president_school.repository;

import com.example.president_school.entity.Course;
import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.enums.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findAllByCourseEmployeeOrderByCreatedDateAsc(Employee employee);
    List<Lesson> findAllByCourseGradeAndCourseEmployeeOrderByCreatedDateAsc(Integer grade, Employee employee);
    List<Lesson> findAllByCourseOrderByCreatedDateAsc(Course course);
    int countAllByCourse(Course course);
    boolean existsByLessonType(LessonType lessonType);
    Optional<Lesson> findByLessonType(LessonType lessonType);
    Optional<Lesson> findByHashId(String hashId);
}
