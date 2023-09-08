package com.example.president_school.repository;

import com.example.president_school.entity.PersonImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonImageRepository extends JpaRepository<PersonImage, Integer> {
    PersonImage findByHashId(String hashId);
}
