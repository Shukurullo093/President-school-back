package com.example.president_school.repository;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Student;
import com.example.president_school.entity.StudentTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentTaskStatusRepository extends JpaRepository<StudentTaskStatus, Integer> {
    List<StudentTaskStatus> findByStudentAndLesson(Student student, Lesson lesson);
    boolean existsByStudentAndLessonAndTaskOrder(Student student, Lesson lesson, int order);
    List<StudentTaskStatus> findAllByStudentIdAndLessonId(Long studentId, UUID lessonId);
}
