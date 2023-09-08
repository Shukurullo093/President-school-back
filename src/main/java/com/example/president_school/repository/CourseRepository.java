package com.example.president_school.repository;

import com.example.president_school.entity.Course;
import com.example.president_school.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByGradeAndEmployee(Integer integer, Employee employee);
}
