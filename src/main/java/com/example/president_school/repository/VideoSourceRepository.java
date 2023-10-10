//package com.example.president_school.repository;
//
//import com.example.president_school.entity.Lesson;
//import com.example.president_school.entity.VideoSource;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface VideoSourceRepository extends JpaRepository<VideoSource, Integer> {
//    Optional<VideoSource> findByHashId(String hashId);
//    Optional<VideoSource> findByLesson(Lesson lesson);
//    Optional<VideoSource> findByLessonId(UUID lessonId);
//}
