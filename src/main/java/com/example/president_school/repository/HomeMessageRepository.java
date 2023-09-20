package com.example.president_school.repository;

import com.example.president_school.entity.HomeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMessageRepository extends JpaRepository<HomeMessage, Integer> {
    List<HomeMessage> findByMessageStatus(boolean messageStatus);
}
