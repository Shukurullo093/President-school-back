package com.example.president_school.repository;

import com.example.president_school.entity.TestImageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestImgSourceRepository extends JpaRepository<TestImageSource, Integer> {
    Optional<TestImageSource> findByHashId(String hashId);
}
