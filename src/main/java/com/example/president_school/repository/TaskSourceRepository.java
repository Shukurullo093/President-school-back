package com.example.president_school.repository;

import com.example.president_school.entity.TaskSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskSourceRepository extends JpaRepository<TaskSource, Integer> {
    Optional<TaskSource> findByHashId(String hashId);
}
