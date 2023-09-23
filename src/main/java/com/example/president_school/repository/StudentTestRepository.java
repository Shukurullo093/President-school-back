package com.example.president_school.repository;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Student;
import com.example.president_school.entity.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, Integer> {
    Optional<StudentTest> findByStudentAndTestLesson(Student student, Lesson lesson);
}
