package com.example.president_school.repository;

import com.example.president_school.entity.StudentTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, Integer> {
}
