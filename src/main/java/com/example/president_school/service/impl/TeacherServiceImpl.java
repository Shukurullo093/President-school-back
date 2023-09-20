package com.example.president_school.service.impl;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.*;
import com.example.president_school.service.GeneralService;
import com.example.president_school.service.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;
    private final VideoSourceRepository videoSourceRepository;
    private final TaskSourceRepository taskSourceRepository;
    private final TestRepository testRepository;
    private final TestImgSourceRepository testImgSourceRepository;
    private final GeneralService generalService;

    @Value("${upload.folder}")
    private String uploadFolder;

    @Transactional
    @Override
    public ControllerResponse addLesson(String title, String description, String type, Integer grade,
                                        MultipartFile video, MultipartFile task) {
        if (type.equals("VIDEO") && video.isEmpty()){
            return new ControllerResponse("Dars videosi topilmadi.", 301);
        }
        if (type.equals("VIDEO") && task.isEmpty()){
            return new ControllerResponse("Dars topshirig'i topilmadi.", 301);
        }
        Lesson lesson = new Lesson();
        Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(grade,
                employeeRepository.findByPhone("+998930024547").get());
        lesson.setCourse(courseOptional.get());
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setLessonType(LessonType.valueOf(type.toUpperCase()));

        Lesson lesson1 = lessonRepository.save(lesson);
        if (!LessonType.valueOf(type.toUpperCase()).equals(LessonType.TEST)){
            // *********** save video ****************
            File uploadFolder = new File(String.format("%s/%d/%s/VIDEO/",
                    this.uploadFolder,
                    lesson.getCourse().getId(),
                    LessonType.VIDEO));
            if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
                System.out.println(uploadFolder);
            }
            VideoSource videoSource = new VideoSource();
            videoSource.setLesson(lesson1);
            videoSource.setContentType(video.getContentType());
            videoSource.setName(video.getOriginalFilename());
            videoSource.setExtension(generalService.getExtension(video.getOriginalFilename()));
            videoSource.setFileSize(video.getSize());
            videoSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
            videoSource.setUploadPath(String.format("%d/%s/VIDEO/%s.%s",
                    lesson.getCourse().getId(),
                    LessonType.VIDEO,
                    videoSource.getHashId(),
                    videoSource.getExtension()));
            videoSourceRepository.save(videoSource);

            uploadFolder = uploadFolder.getAbsoluteFile();
            File file1 = new File(uploadFolder, String.format("%s.%s",
                    videoSource.getHashId(),
                    videoSource.getExtension()));
            try {
                video.transferTo(file1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // *********** save task ****************

            File uploadTaskFolder = new File(String.format("%s/%d/%s/TASK/",
                    this.uploadFolder,
                    lesson.getCourse().getId(),
                    LessonType.VIDEO));
            if (!uploadTaskFolder.exists() && uploadTaskFolder.mkdirs()) {
                System.out.println(uploadTaskFolder);
            }
            TaskSource taskSource = new TaskSource();
            taskSource.setLesson(lesson1);
            taskSource.setContentType(task.getContentType());
            taskSource.setName(task.getOriginalFilename());
            taskSource.setExtension(generalService.getExtension(task.getOriginalFilename()));
            taskSource.setFileSize(task.getSize());
            taskSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
            taskSource.setUploadPath(String.format("%d/%s/TASK/%s.%s",
                    lesson.getCourse().getId(),
                    LessonType.VIDEO,
                    taskSource.getHashId(),
                    taskSource.getExtension()));
            taskSourceRepository.save(taskSource);

            uploadTaskFolder = uploadTaskFolder.getAbsoluteFile();
            File file12 = new File(uploadTaskFolder, String.format("%s.%s",
                    taskSource.getHashId(),
                    taskSource.getExtension()));
            try {
                task.transferTo(file12);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ControllerResponse("Dars muvaffaqqiyatli yaratildi.", 200);
    }

    @Transactional
    @Override
    public ControllerResponse addTest(String lessonId, String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img,
                                      String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img) {
        Test test = new Test();
        test.setLesson(lessonRepository.findById(UUID.fromString(lessonId)).get());
        test.setQuestionTxt(question);
        test.setAnswer1(ans1);
        test.setAnswer2(ans2);
        test.setAnswer3(ans3);
        if (!questionImg.isEmpty())
            test.setQuestionImg(getSource(questionImg, lessonId,
                lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
        if (!ans1Img.isEmpty())
            test.setAnswer1Img(getSource(ans1Img, lessonId,
                    lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
        if (!ans2Img.isEmpty())
            test.setAnswer2Img(getSource(ans2Img, lessonId,
                    lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
        if (!ans3Img.isEmpty())
            test.setAnswer3Img(getSource(ans3Img, lessonId,
                    lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
        testRepository.save(test);
        return new ControllerResponse("Test yaratildi", 200);
    }

    private TestImageSource getSource(MultipartFile file, String sourceType, Integer course) {
        File uploadFolder = new File(String.format("%s/%d/TEST/%s/",
                this.uploadFolder,
                course,
                sourceType));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println(uploadFolder);
        }
        TestImageSource testImageSource = new TestImageSource();
        testImageSource.setContentType(file.getContentType());
        testImageSource.setName(file.getOriginalFilename());
        testImageSource.setExtension(generalService.getExtension(file.getOriginalFilename()));
        testImageSource.setFileSize(file.getSize());
        testImageSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
        testImageSource.setUploadPath(String.format("%d/TEST/%s/%s.%s",
                course,
                sourceType,
                testImageSource.getHashId(),
                testImageSource.getExtension()));
        TestImageSource save = testImgSourceRepository.save(testImageSource);

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file1 = new File(uploadFolder, String.format("%s.%s",
                testImageSource.getHashId(),
                testImageSource.getExtension()));
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return save;
    }

    @Transactional(readOnly = true)
    @Override
    public VideoSource getVideo(String hashId) {
        Optional<VideoSource> byHashId = videoSourceRepository.findByHashId(hashId);
        return byHashId.get();
    }

    @Transactional(readOnly = true)
    @Override
    public TaskSource getPdf(String hashId) {
        Optional<TaskSource> byHashId = taskSourceRepository.findByHashId(hashId);
        return byHashId.get();
    }

    @Transactional
    @Override
    public ControllerResponse editLesson(String id, String title, String description,
                                         String type, MultipartFile video, MultipartFile task) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(id));
        Lesson lesson = lessonOptional.get();
        lesson.setTitle(title);
        lesson.setDescription(description);
        Lesson save = lessonRepository.save(lesson);

        if (!video.isEmpty()){
            VideoSource videoSource = videoSourceRepository
                    .findById(videoSourceRepository.findByLessonId(lesson.getId()).get().getId()).get();
            File file1 = new File(String.format("%s/%s",
                    this.uploadFolder,
                    videoSource.getUploadPath()));
            if (file1.delete()) {
                System.out.println("deleted old test file mp4");
            }
            videoSource.setLesson(save);
            File uploadFolder = new File(String.format("%s/%d/%s/VIDEO/",
                    this.uploadFolder,
                    lesson.getCourse().getId(),
                    LessonType.VIDEO));
            if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
                System.out.println(uploadFolder);
            }
            videoSource.setContentType(video.getContentType());
            videoSource.setName(video.getOriginalFilename());
            videoSource.setExtension(generalService.getExtension(video.getOriginalFilename()));
            videoSource.setFileSize(video.getSize());
            videoSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
            videoSource.setUploadPath(String.format("%d/%s/VIDEO/%s.%s",
                    lesson.getCourse().getId(),
                    LessonType.VIDEO,
                    videoSource.getHashId(),
                    videoSource.getExtension()));
            videoSourceRepository.save(videoSource);

            uploadFolder = uploadFolder.getAbsoluteFile();
            File file12 = new File(uploadFolder, String.format("%s.%s",
                    videoSource.getHashId(),
                    videoSource.getExtension()));
            try {
                video.transferTo(file12);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!task.isEmpty()){
            TaskSource taskSource = taskSourceRepository.findById(taskSourceRepository.findByLessonId(save.getId()).get().getId()).get();

            File file1 = new File(String.format("%s/%s",
                    this.uploadFolder,
                    taskSource.getUploadPath()));
            if (file1.delete()) {
                System.out.println("deleted old test file task pdf");
            }

            File uploadTaskFolder = new File(String.format("%s/%d/%s/TASK/",
                    this.uploadFolder,
                    lesson.getCourse().getId(),
                    LessonType.VIDEO));
            if (!uploadTaskFolder.exists() && uploadTaskFolder.mkdirs()) {
                System.out.println(uploadTaskFolder);
            }
//            TaskSource taskSource = new TaskSource();
            taskSource.setLesson(save);
            taskSource.setContentType(task.getContentType());
            taskSource.setName(task.getOriginalFilename());
            taskSource.setExtension(generalService.getExtension(task.getOriginalFilename()));
            taskSource.setFileSize(task.getSize());
            taskSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
            taskSource.setUploadPath(String.format("%d/%s/TASK/%s.%s",
                    lesson.getCourse().getId(),
                    LessonType.VIDEO,
                    taskSource.getHashId(),
                    taskSource.getExtension()));
            taskSourceRepository.save(taskSource);

            uploadTaskFolder = uploadTaskFolder.getAbsoluteFile();
            File file12 = new File(uploadTaskFolder, String.format("%s.%s",
                    taskSource.getHashId(),
                    taskSource.getExtension()));
            try {
                task.transferTo(file12);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ControllerResponse("Dars tahrirlandi", 200);
    }

    @Override
    public void deleteTest(String id) {
        final Optional<Test> testOptional = testRepository.findById(Integer.valueOf(id));
        final Test test = testOptional.get();
        if (test.getQuestionImg() != null){
            File file = new File(uploadFolder + "/" + test.getQuestionImg().getUploadPath());
            file.delete();
        }
        if (test.getAnswer1Img() != null){
            File file = new File(uploadFolder + "/" + test.getAnswer1Img().getUploadPath());
            file.delete();
        }
        if (test.getAnswer2Img() != null){
            File file = new File(uploadFolder + "/" + test.getAnswer2Img().getUploadPath());
            file.delete();
        }
        if (test.getAnswer3Img() != null){
            File file = new File(uploadFolder + "/" + test.getAnswer3Img().getUploadPath());
            file.delete();
        }
        testRepository.deleteById(Integer.valueOf(id));
    }
}
