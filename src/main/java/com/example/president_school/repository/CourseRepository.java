package com.example.president_school.repository;

import com.example.president_school.entity.Course;
import com.example.president_school.entity.Employee;
import com.example.president_school.entity.enums.Science;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByGradeAndEmployee(Integer integer, Employee employee);
    boolean existsByScienceAndGrade(Science science, Integer grade);
    boolean existsByScience(Science valueOf);
    List<Course> findAllByGrade(Integer grade);
    Optional<Course> findByScienceAndGrade(Science science, int grade);
    List<Course> findAllByScience(Science science);
}
