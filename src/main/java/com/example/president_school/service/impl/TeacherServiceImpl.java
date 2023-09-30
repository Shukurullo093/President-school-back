package com.example.president_school.service.impl;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.repository.*;
import com.example.president_school.service.GeneralService;
import com.example.president_school.service.TeacherService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import java.awt.*;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

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

    @Override
    public ControllerResponse editTest(String lessonId, Integer testId, String question, MultipartFile questionImg, String ans1,
                                       MultipartFile ans1Img, String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img) {
        final Optional<Test> testOptional = testRepository.findById(testId);
        if (testOptional.isPresent()) {
            final Test test = testOptional.get();
            test.setQuestionTxt(question);
            test.setAnswer1(ans1);
            test.setAnswer2(ans2);
            test.setAnswer3(ans3);
            if (!questionImg.isEmpty()){
                int id = test.getQuestionImg() == null ? 0 : test.getQuestionImg().getId();
                test.setQuestionImg(updateSource(questionImg, id, lessonId, lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
            }
            if (!ans1Img.isEmpty()) {
                int id = test.getAnswer1Img() == null ? 0 : test.getAnswer1Img().getId();
                test.setAnswer1Img(updateSource(ans1Img, id, lessonId, lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
            }
            if (!ans2Img.isEmpty()) {
                int id = test.getAnswer2Img() == null ? 0 : test.getAnswer2Img().getId();
                test.setAnswer2Img(updateSource(ans2Img, id, lessonId, lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
            }
            if (!ans3Img.isEmpty()) {
                int id = test.getAnswer3Img() == null ? 0 : test.getAnswer3Img().getId();
                test.setAnswer3Img(updateSource(ans3Img, id, lessonId, lessonRepository.findById(UUID.fromString(lessonId)).get().getCourse().getId()));
            }
            testRepository.save(test);
            return new ControllerResponse("Test tahrirlandi", 200);
        }
        return new ControllerResponse("Test topilmadi", 208);
    }

    private TestImageSource getSource(MultipartFile file, String sourceType, Integer course) {
        File uploadFolder = new File(String.format("%s/%d/TEST/%s/",
                this.uploadFolder,
                course,
                sourceType));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println(uploadFolder + " path created for test");
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

    private TestImageSource updateSource(MultipartFile file, Integer id, String sourceType, Integer course){
        File uploadFolder = new File(String.format("%s/%d/TEST/%s/",
                this.uploadFolder,
                course,
                sourceType));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println(uploadFolder + " path created for test");
        }
        final Optional<TestImageSource> byId = testImgSourceRepository.findById(id);
        TestImageSource testImageSource;
        testImageSource = byId.orElseGet(TestImageSource::new);

        File file1 = new File(uploadFolder + "/" + testImageSource.getUploadPath());
        if (file1.delete()) {
            System.out.println("deleted " + file1);
        }

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
        File file12 = new File(uploadFolder, String.format("%s.%s",
                testImageSource.getHashId(),
                testImageSource.getExtension()));
        try {
            file.transferTo(file12);
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
//      ***************************************
    @Override
    public void exportLessonToExcel(HttpServletResponse response) throws IOException {
        final Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        final Employee employee = employeeOptional.get();
        if (employee.getGrade().equals("Hammasi")){
            List<Lesson> lessonList = new ArrayList<>();
            for (int i = 2; i < 5; i++){
                final Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(i, employee);
                final List<Lesson> allByCourseOrderByCreatedDateAsc =
                        lessonRepository.findAllByCourseOrderByCreatedDateAsc(courseOptional.get());
                lessonList.addAll(allByCourseOrderByCreatedDateAsc);
            }
            exportToExcel(lessonList, response);
        } else {
            final Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(Integer.valueOf(employee.getGrade()), employee);
            final List<Lesson> allByCourseOrderByCreatedDateAsc = lessonRepository.findAllByCourseOrderByCreatedDateAsc(courseOptional.get());
            exportToExcel(allByCourseOrderByCreatedDateAsc, response);
        }
    }

    public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String prefix){
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = dataFormat.format(new Date());
        String fileName = prefix + timeStamp + extension;
        response.setContentType(contentType);
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    public void exportToExcel(List<Lesson> lessonList, HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        setResponseHeader(response, "application/octet-stream", ".xlsx", "Darslar_");
        writeHeaderLine();
        writeDataLine(lessonList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

    private void writeDataLine(List<Lesson> lessonList) {
        int rowIndex = 1;
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFamily(FontFamily.ROMAN);
        font.setBold(false);
        font.setFontHeight(14);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        for (Lesson lesson : lessonList) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
//            if (lesson.getCourse().getGrade().equals("2")){
//                cellStyle.setFillForegroundColor(IndexedColors.DARK_RED.index);
//                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            } else if (lesson.getCourse().getGrade().equals("3")){
//                cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
//                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            }
            createCell(row, columnIndex++, rowIndex - 1, cellStyle);
            createCell(row, columnIndex++, lesson.getTitle(), cellStyle);
            createCell(row, columnIndex++, lesson.getDescription(), cellStyle);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
            createCell(row, columnIndex++, dateFormat.format(lesson.getCreatedDate()), cellStyle);
            createCell(row, columnIndex++, lesson.getCourse().getGrade(), cellStyle);
            createCell(row, columnIndex++, lesson.getLessonType().name(), cellStyle);
            if (!lesson.getLessonType().equals(LessonType.TEST)){
                final Optional<VideoSource> videoSourceOptional = videoSourceRepository.findByLessonId(lesson.getId());
                final VideoSource videoSource = videoSourceOptional.get();
                createCell(row, columnIndex++, videoSource.getName(), cellStyle);
                createCell(row, columnIndex++, videoSource.getFileSize() / 1024 / 1024 + "MB", cellStyle);
            } else {
                createCell(row, columnIndex++, "", cellStyle);
                createCell(row, columnIndex++, "", cellStyle);
            }
        }
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Darslar");
        XSSFRow row=sheet.createRow(0);
        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        font.setFamily(FontFamily.DECORATIVE);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);
        createCell(row, 0, "No.", cellStyle);
        createCell(row, 1, "Mavzu", cellStyle);
        createCell(row, 2, "Izoh", cellStyle);
        createCell(row, 3, "Yaratilgan vaqt", cellStyle);
        createCell(row, 4, "Sinf", cellStyle);
        createCell(row, 5, "Tur", cellStyle);
        createCell(row, 6, "Video nomi", cellStyle);
        createCell(row, 7, "Hajm", cellStyle);
    }

    private void createCell(XSSFRow row, int i, Object value, XSSFCellStyle cellStyle) {
        XSSFCell cell = row.createCell(i);
        sheet.autoSizeColumn(i);
        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        } else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        } else{
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(cellStyle);
    }
//    ************************************
    @Override
    public void exportLessonToPdf(HttpServletResponse response) throws IOException {
        final Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998930024547");
        final Employee employee = employeeOptional.get();
        if (employee.getGrade().equals("Hammasi")){
            List<Lesson> lessonList = new ArrayList<>();
            for (int i = 2; i < 5; i++){
                final Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(i, employee);
                final List<Lesson> allByCourseOrderByCreatedDateAsc =
                        lessonRepository.findAllByCourseOrderByCreatedDateAsc(courseOptional.get());
                lessonList.addAll(allByCourseOrderByCreatedDateAsc);
            }
            exportToPdf(lessonList, response);
        } else {
            final Optional<Course> courseOptional = courseRepository.findByGradeAndEmployee(Integer.valueOf(employee.getGrade()), employee);
            final List<Lesson> allByCourseOrderByCreatedDateAsc = lessonRepository.findAllByCourseOrderByCreatedDateAsc(courseOptional.get());
            exportToPdf(allByCourseOrderByCreatedDateAsc, response);
        }
    }

    public void exportToPdf(List<Lesson> lessonList, HttpServletResponse response) throws IOException {
        setResponseHeader(response, "application/pdf", ".pdf", "Darslar Ro'yhati_");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
//        document.set
        document.open();

    //        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font font = FontFactory.getFont("Times New Roman");
        font.setSize(14);
        font.setColor(Color.BLACK);

        Paragraph paragraph=new Paragraph("Guruhlar ro'yhati", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable table=new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        writeGroupHeader(table);
        writeGroupData(table, lessonList);
        document.add(table);

        document.close();
    }

    private void writeGroupData(PdfPTable table, List<Lesson> lessonList) {
        int i = 1;
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        for (Lesson lesson : lessonList) {
            table.addCell(String.valueOf(i++));
            table.addCell(lesson.getTitle());
            table.getDefaultCell().setBackgroundColor(Color.BLUE);
            table.addCell(lesson.getDescription());
            table.getDefaultCell().setBackgroundColor(Color.WHITE);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            table.addCell(dateFormat.format(lesson.getCreatedDate()));
            table.addCell(lesson.getCourse().getGrade().toString());
            table.addCell(lesson.getLessonType().name());
            if (!lesson.getLessonType().equals(LessonType.TEST)){
                final Optional<VideoSource> videoSourceOptional = videoSourceRepository.findByLessonId(lesson.getId());
                final VideoSource videoSource = videoSourceOptional.get();
                table.addCell(videoSource.getName());
                table.addCell(videoSource.getFileSize() / 1024 / 1024 + "MB");
            } else {
                table.addCell("");
                table.addCell("");
            }

        }
    }

    private void writeGroupHeader(PdfPTable table) {
        PdfPCell pdfPCell=new PdfPCell();
        pdfPCell.setBackgroundColor(Color.GREEN);
        pdfPCell.setPadding(5);

        Font font= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.white);

        pdfPCell.setPhrase(new Phrase("#", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Mavzu", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Izoh", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Yaratilgan vaqt", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Sinf", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Tur", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Video nomi", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
        pdfPCell.setPhrase(new Phrase("Hajm", font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(pdfPCell);
    }
}

// $('#statusBtn').click(function(){
//        let date=document.getElementById("groupDate1").value.toString();
//        let value = $("select#statusSlc option:selected").val();
//        $.ajax({
//            type:"GET",
//            contentType: "application/json",
//            url: baseUrl+"/d1/print/"+value+"/"+date,
//            success: function(data) {
//                let restorePage = document.body.innerHTML;
//                document.body.innerHTML = data;
//                window.print();
//                document.body.innerHTML = restorePage;
//            }
//        })
//    });