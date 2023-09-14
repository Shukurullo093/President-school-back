package com.example.president_school.repository;

import com.example.president_school.entity.AccessCourse;
import com.example.president_school.entity.Course;
import com.example.president_school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessCourseRepository extends JpaRepository<AccessCourse, Long> {
    boolean existsByCourseAndStudent(Course course, Student student);
    List<AccessCourse> findAllByStudent(Student student);
}
