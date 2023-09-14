package com.example.president_school.service.impl;

import com.example.president_school.entity.AccessCourse;
import com.example.president_school.entity.Course;
import com.example.president_school.entity.PersonImage;
import com.example.president_school.entity.Student;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.AccessCourseRepository;
import com.example.president_school.repository.CourseRepository;
import com.example.president_school.repository.PersonImageRepository;
import com.example.president_school.repository.StudentRepository;
import com.example.president_school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final PersonImageRepository personImageRepository;
    private final CourseRepository courseRepository;
    private final AccessCourseRepository accessCourseRepository;
    @Value("${upload.folder}")
    private String uploadFolder;

    @Override
    public ControllerResponse register(StudentDto studentDto) {
        final boolean b = studentRepository.existsByPhone(studentDto.getPhone());
        if (b) {
            return new ControllerResponse("Telefon raqam allaqachon ro'yhatdan o'tgan", 208);
        }
        Student student = new Student();
        student.setFullName(studentDto.getFullName());
        student.setPhone(studentDto.getPhone());
        student.setPassword(studentDto.getPassword());
        student.setGender(studentDto.getGender());
        student.setGrade(Integer.parseInt(studentDto.getGrade()));
        student.setRole(Role.STUDENT);

        if (!studentDto.getImage().isEmpty()){
            File uploadFolder = new File(String.format("%s/person_images/",
                    this.uploadFolder));
            if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
            }
            MultipartFile image = studentDto.getImage();
            PersonImage personImage = new PersonImage();
            personImage.setContentType(image.getContentType());
            personImage.setName(image.getOriginalFilename());
            personImage.setExtension(getExtension(image.getOriginalFilename()));
            personImage.setFileSize(image.getSize());
            personImage.setHashId(UUID.randomUUID().toString().substring(0, 10));
            personImage.setUploadPath(String.format("person_images/%s.%s",
                    personImage.getHashId(),
                    personImage.getExtension()));
            PersonImage personImage1 = personImageRepository.save(personImage);
            uploadFolder = uploadFolder.getAbsoluteFile();
            File file = new File(uploadFolder, String.format("%s.%s",
                    personImage.getHashId(),
                    personImage.getExtension()));
            try {
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            student.setImage(personImage1);
        }
        studentRepository.save(student);
        return new ControllerResponse("Muvaffaqqiyatli ro'yhatdan o'tdingiz. Tizimga kiritshingiz mumkin.", 201);
    }

    @Override
    public String shop(String courseId, String s) {
        final Optional<Course> courseOptional = courseRepository.findById(Integer.parseInt(courseId));
        if (courseOptional.isPresent()) {
             Course course = courseOptional.get();
            AccessCourse accessCourse = new AccessCourse();
            accessCourse.setCourse(course);
            accessCourse.setStudent(studentRepository.findByPhone(s).get());
            if (!accessCourseRepository.existsByCourseAndStudent(accessCourse.getCourse(), accessCourse.getStudent())) {
                accessCourseRepository.save(accessCourse);
            }
            return "Kurs xarid qilindi";
        }
        return null;
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
}
