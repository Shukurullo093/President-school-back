package com.example.president_school.repository;

import com.example.president_school.entity.Employee;
import com.example.president_school.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByCourseEmployeeOrderByCreatedDateAsc(Employee employee);
    List<Lesson> findAllByCourseGradeAndCourseEmployee(Integer grade, Employee employee);
}
