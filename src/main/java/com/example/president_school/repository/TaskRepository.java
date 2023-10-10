package com.example.president_school.repository;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Task;
import com.example.president_school.entity.TaskSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Integer countAllByLesson(Lesson lesson);
    List<Task> findByLessonIdOrderByOrderNumber(UUID id);
    Optional<TaskSource> findByTaskImgHashId(String hashId);
}
