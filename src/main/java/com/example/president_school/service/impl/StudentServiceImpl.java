package com.example.president_school.service.impl;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.StudentDto;
import com.example.president_school.repository.*;
import com.example.president_school.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final PersonImageRepository personImageRepository;
    private final CourseRepository courseRepository;
    private final AccessCourseRepository accessCourseRepository;
    private final AccessLessonRepository accessLessonRepository;
    private final StudentTaskStatusRepository studentTaskStatusRepository;
    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;

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

    @Override
    public void exportTestResultToPdf(HttpServletResponse response) {

    }
//  ************************
    @Transactional
    @Override
    public ControllerResponse checkTask(Student student, Integer taskId, String answer) {
        final Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            final Task task = taskOptional.get();
            if (task.getAnswer().equals(answer)){
                studentTaskStatusRepository.save(new StudentTaskStatus(student, task.getLesson(), task.getOrderNumber()));
                final List<StudentTaskStatus> byStudentAndLesson = studentTaskStatusRepository.findByStudentAndLesson(student, task.getLesson());
                final Integer taskCountByLesson = taskRepository.countAllByLesson(task.getLesson());
                if (byStudentAndLesson.size() == taskCountByLesson){
                    final List<StudentTaskStatus> allByStudentIdAndLessonId = studentTaskStatusRepository.findAllByStudentIdAndLessonId(student.getId(), task.getLesson().getId());
                    studentTaskStatusRepository.deleteAll(allByStudentIdAndLessonId);
                    final Optional<Lesson> byCourseIdAndOrderNumber = lessonRepository.findByCourseIdAndOrderNumber(task.getLesson().getCourse().getId(), task.getLesson().getOrderNumber() + 1);
                    if (byCourseIdAndOrderNumber.isPresent()) {
                        accessLessonRepository.save(new AccessLesson(student, byCourseIdAndOrderNumber.get()));
                    }
                    return new ControllerResponse("Tabriklaymiz! Siz barcha topshiriqlarni bajardingiz. Keyingi darsga o'tishingiz mumkin.", 200, "true");
                } else {
                    return new ControllerResponse("Ajoyib, To'g'ri javob berdingiz.", 200, "false");
                }
            }
            else {
                return new ControllerResponse("Afsus, Biroq javobingiz xato, namuna bilan tanishib qaytadan urinib ko'ring.", 208);
            }
        }
        return new ControllerResponse("topshiriq topilmadi", 404);
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

//    public void exportToPdf(List<Group> groupList, HttpServletResponse response) throws IOException {
//        setResponseHeader(response, "application/pdf", ".pdf", "Guruh_");
//        Document document=new Document(PageSize.A4);
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//
////        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        Font font = FontFactory.getFont("Times New Roman");
//        font.setSize(18);
//        font.setColor(Color.BLACK);
//
//        Paragraph paragraph=new Paragraph("Guruhlar ro'yhati", font);
//        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(paragraph);
//
//        PdfPTable table=new PdfPTable(8);
//        table.setWidthPercentage(100f);
//        table.setSpacingBefore(10);
//        writeGroupHeader(table);
//        writeGroupData(table, groupList);
//        document.add(table);
//
//        document.close();
//    }
//
//    private void writeGroupData(PdfPTable table, List<Group> groupList) {
//        int i=1;
//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//        for (Group group : groupList) {
//            table.addCell(String.valueOf(i++));
//            table.addCell(group.getName());
//            table.getDefaultCell().setBackgroundColor(Color.BLUE);
//            table.addCell(group.getScience().getName());
//            table.getDefaultCell().setBackgroundColor(Color.WHITE);
//            table.addCell(group.getTeacher().getLastname()+" "+group.getTeacher().getFirstname());
//            String days="";
//            for (String day : group.getDays()) {
//                days+=day+"\n";
//            }
//            days+=group.getLessontime();
//            table.addCell(days);
//            table.addCell(String.valueOf(group.getRoom().getNumber()));
//            table.addCell(String.valueOf(group.getPayment()));
//            table.addCell(group.getComment());
//        }
//    }
//
//    private void writeGroupHeader(PdfPTable table) {
//        PdfPCell pdfPCell=new PdfPCell();
//        pdfPCell.setBackgroundColor(Color.GREEN);
//        pdfPCell.setPadding(5);
//
//        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setColor(Color.white);
//
//        pdfPCell.setPhrase(new Phrase("#", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("Nomi", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("Fan", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("O'qituvchi", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("Vaqt", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("Xona", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("To'lov", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//        pdfPCell.setPhrase(new Phrase("Ta'rif", font));
//        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell(pdfPCell);
//    }
}
