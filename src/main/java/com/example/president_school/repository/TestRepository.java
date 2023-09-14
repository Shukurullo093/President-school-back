package com.example.president_school.repository;

import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findAllByLesson(Lesson lesson);
}