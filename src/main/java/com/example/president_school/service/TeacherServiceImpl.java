package com.example.president_school.service;

import com.example.president_school.entity.Course;
import com.example.president_school.entity.Lesson;
import com.example.president_school.entity.LessonSource;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.CourseRepository;
import com.example.president_school.repository.EmployeeRepository;
import com.example.president_school.repository.LessonRepository;
import com.example.president_school.repository.LessonSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;
    private final LessonSourceRepository lessonSourceRepository;
    @Value("${upload.folder}")
    private String uploadFolder;

    @Transactional
    @Override
    public ControllerResponse addLesson(String title, String description, String type, Integer grade,
                                        MultipartFile source, MultipartFile task, MultipartFile test,
                                        String testAnswer) {
        if (type.equals("VIDEO") && source.isEmpty()){
            return new ControllerResponse("Dars videosi topilmadi.", 301);
        }
        if (type.equals("VIDEO") && task.isEmpty()){
            return new ControllerResponse("Dars topshirig'i topilmadi.", 301);
        }
        if (type.equals("TEST") && test.isEmpty()){
            return new ControllerResponse("Test fayli topilmadi.", 301);
        }
        if (type.equals("TEST") && testAnswer.isEmpty()){
            return new ControllerResponse("Test javoblarini kiriting.", 301);
        }
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setLessonType(LessonType.valueOf(type));
        Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(grade,
                employeeRepository.findByPhone("+998930024547").get());
        lesson.setCourse(courseOptional.get());
        if (lesson.getLessonType().equals(LessonType.TEST)){
            lesson.setTestAnswer(testAnswer);
            lesson.setLessonTestSource(getSource(test, "TEST", lesson.getCourse().getId()));
        }
        else {
            lesson.setLessonTaskSource(getSource(task, "TASK", lesson.getCourse().getId()));
            lesson.setLessonVideoSource(getSource(source, "VIDEO", lesson.getCourse().getId()));
        }
        lessonRepository.save(lesson);
        return new ControllerResponse("Dars muvaffaqqiyatli yaratildi.", 200);
    }

    private LessonSource getSource(MultipartFile file, String sourceType, Integer course) {
        File uploadFolder = new File(String.format("%s/%d/%s/",
                this.uploadFolder,
                course,
                sourceType));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println(uploadFolder);
        }
        LessonSource lessonSource = new LessonSource();
        lessonSource.setContentType(file.getContentType());
        lessonSource.setName(file.getOriginalFilename());
        lessonSource.setExtension(getExtension(file.getOriginalFilename()));
        lessonSource.setFileSize(file.getSize());
        lessonSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
        lessonSource.setUploadPath(String.format("%d/%s/%s.%s",
                course,
                sourceType,
                lessonSource.getHashId(),
                lessonSource.getExtension()));
        LessonSource save = lessonSourceRepository.save(lessonSource);

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file1 = new File(uploadFolder, String.format("%s.%s",
                lessonSource.getHashId(),
                lessonSource.getExtension()));
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return save;
    }

    private String getExtension(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }

    @Transactional(readOnly = true)
    @Override
    public LessonSource getVideo(String id) {
        Optional<LessonSource> byHashId = lessonSourceRepository.findById(Integer.valueOf(id));
        return byHashId.get();
    }

    @Transactional
    @Override
    public ControllerResponse editLesson(String id, String title, String description, String type,
                                         MultipartFile source, MultipartFile task, MultipartFile test, String testAnswer) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(Integer.valueOf(id));
        Lesson lesson = lessonOptional.get();
        lesson.setTitle(title);
        lesson.setDescription(description);
        if (lesson.getLessonType()==LessonType.TEST && !test.isEmpty()){
            File file1 = new File(String.format("%s/%s",
                    this.uploadFolder,
                    lesson.getLessonTestSource().getUploadPath()));
            if (file1.delete()) {
                System.out.println("deleted old test file");
            }
            LessonSource lessonTestSource = lesson.getLessonTestSource();
            lesson.setLessonTestSource(getSource(test, "TEST", lesson.getCourse().getId()));
            lessonSourceRepository.delete(lessonTestSource);
        }
        if (lesson.getLessonType()==LessonType.VIDEO && !source.isEmpty()){
            File file1 = new File(String.format("%s/%s",
                    this.uploadFolder,
                    lesson.getLessonVideoSource().getUploadPath()));
            if (file1.delete()) {
                System.out.println("deleted old video file");
            }
            LessonSource lessonVideoSource = lesson.getLessonVideoSource();
            lesson.setLessonVideoSource(getSource(source, "VIDEO", lesson.getCourse().getId()));
            lessonSourceRepository.delete(lessonVideoSource);
        }
        if (lesson.getLessonType()==LessonType.VIDEO && !task.isEmpty()){
            File file1 = new File(String.format("%s/%s",
                    this.uploadFolder,
                    lesson.getLessonTaskSource().getUploadPath()));
            if (file1.delete()) {
                System.out.println("deleted old task file");
            }
            LessonSource lessonTaskSource = lesson.getLessonTaskSource();
            lesson.setLessonTaskSource(getSource(task, "TASK", lesson.getCourse().getId()));
            lessonSourceRepository.delete(lessonTaskSource);
        }
        lesson.setTestAnswer(testAnswer);
        lessonRepository.save(lesson);
        return new ControllerResponse("Dars ma'lumotlari muvaffaqqiyatli o'zgartirildi.", 200);
    }
}
