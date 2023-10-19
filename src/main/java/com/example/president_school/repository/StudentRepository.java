package com.example.president_school.repository;

import com.example.president_school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByPhone(String phone);
    Optional<Student> findByPhone(String phone);
}
