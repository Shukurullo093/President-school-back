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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
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
    private final LessonRepository lessonRepository;
//    private final ChatRepository chatRepository;

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

//    @Override
//    public ControllerResponse sendMsg(Student student, String lessonId, Integer taskOrder, String text, MultipartFile image) {
//        Chat chat = new Chat();
//        chat.setStudent(student);
//        chat.setMessage(text);
//        chat.setMessageOwner(Role.STUDENT);
//        final Optional<Lesson> lessonOptional = lessonRepository.findById(UUID.fromString(lessonId));
//        chat.setLesson(lessonOptional.get());
//        chat.setTaskOrder(taskOrder);
//        if (!image.isEmpty()){
//            File uploadFolder = new File(String.format("%s/CHAT/",
//                    this.uploadFolder));
//            if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders. for chat images");
//            }
//
//            chat.setContentType(image.getContentType());
//            chat.setName(image.getOriginalFilename());
//            chat.setExtension(getExtension(image.getOriginalFilename()));
//            chat.setFileSize(image.getSize());
//            chat.setHashId(UUID.randomUUID().toString().substring(0, 10));
//            chat.setUploadPath(String.format("CHAT/%s.%s",
//                    chat.getHashId(),
//                    chat.getExtension()));
//
//            uploadFolder = uploadFolder.getAbsoluteFile();
//            File file = new File(uploadFolder, String.format("%s.%s",
//                    chat.getHashId(),
//                    chat.getExtension()));
//            try {
//                image.transferTo(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        chatRepository.save(chat);
//        return new ControllerResponse("Xabar jo'natildi", 200);
//    }

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
