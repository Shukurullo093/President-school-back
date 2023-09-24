package com.example.president_school.repository;

import com.example.president_school.entity.Chat;
import com.example.president_school.entity.Course;
import com.example.president_school.entity.Student;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByLessonCourseScienceAndMessageOwnerAndViewStatusOrderByCreatedDateDesc(Science science, Role role, Boolean status);
    List<Chat> findByStudentAndLessonCourseOrderByCreatedDateDesc(Student student, Course course);
}
