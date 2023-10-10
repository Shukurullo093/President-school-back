package com.example.president_school.service.impl;

import com.example.president_school.entity.*;
import com.example.president_school.entity.enums.LessonType;
import com.example.president_school.entity.enums.Role;
import com.example.president_school.entity.enums.Science;
import com.example.president_school.payload.ControllerResponse;
import com.example.president_school.payload.LoginDto;
import com.example.president_school.repository.*;
//import com.example.president_school.security.JwtProvider;
import com.example.president_school.service.AdminService;
import com.example.president_school.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final EmployeeRepository employeeRepository;
    private final PersonImageRepository personImageRepository;
    private final CourseRepository courseRepository;
    private final PostRepository postRepository;
    private final HomeMessageRepository homeMessageRepository;
    private final LessonRepository lessonRepository;
    private final GeneralService generalService;
    private final TestImgSourceRepository testImgSourceRepository;
    private final TestRepository testRepository;

    @Value("${upload.folder}")
    private String uploadFolder;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    JwtProvider jwtProvider;
//    @Autowired
//    AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public ControllerResponse addEmployee(String name,
                                          String surname,
                                          String email,
                                          String phone,
                                          String science,
                                          Date birthdate,
                                          String gender,
                                          Date joiningDate,
                                          String pass,
                                          MultipartFile image,
                                          String role,
                                          String grade) {
        final boolean b;
        if (!grade.equals("Hammasi")){
            b = courseRepository.existsByScienceAndGrade(Science.valueOf(science), Integer.valueOf(grade));
        } else {
            b = courseRepository.existsByScience(Science.valueOf(science));
        }
        if (b && Role.valueOf(role.toUpperCase()).equals(Role.TEACHER)){
            return new ControllerResponse("Bu sinfga tegishli boshqa o'qituvchi yaratib bo'lmaydi", 301);
        }

        if (!employeeRepository.existsByEmail(email)){
            if (!employeeRepository.existsByPhone(phone)){
                Employee employee = new Employee();
                employee.setFirstName(name);
                employee.setLastName(surname);
                employee.setEmail(email);
                employee.setPhone(phone);
                employee.setScience(Science.valueOf(science));
                employee.setRole(Role.valueOf(role));
                employee.setGrade(grade);
                employee.setBirthDate(birthdate);
                employee.setJoiningDate(joiningDate);
                employee.setGender(gender);
                employee.setPassword(pass);

                if (!image.isEmpty()) {
                    File uploadFolder = new File(String.format("%s/person_images/",
                            this.uploadFolder));
                    if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
                    }
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
                    employee.setImage(personImage1);
                }
                Employee save = employeeRepository.save(employee);

                if (employee.getRole().equals(Role.TEACHER)){
                    if (grade.equals("Hammasi")){
                        for (int i=2; i<5; i++){
                            Course course = new Course(Science.valueOf(science), i, save);
                            courseRepository.save(course);
                        }
                    }
                    else {
                        Course course = new Course(Science.valueOf(science), Integer.parseInt(grade), save);
                        courseRepository.save(course);
                    }
                }

                return new ControllerResponse("Hodim muvaffaqqiyatli ro'yhatdan o'tkazildi.", 200);
            }
            return new ControllerResponse("Bu telefon raqam allaqachon ro'yhatdan o'tgan", 301);
        }
        return new ControllerResponse("Bu email allaqachon ro'yhatdan o'tgan", 301);
    }

    @Transactional
    @Override
    public ControllerResponse updateEmployee(String employeeId, String name, String surname, String email, String phone, String science,
                                             String role, String grade, Date birthdate, String gender, Date joiningDate, String pass, MultipartFile image) {
        Optional<Employee> employeeOptional = employeeRepository.findById(Integer.valueOf(employeeId));
        if (employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            Optional<Employee> byEmail = employeeRepository.findByEmail(email);
            if (byEmail.isPresent() && byEmail.get().getId() != employee.getId()){
                return new ControllerResponse("Bu email allaqachon ro'yhatdan o'tgan", 301);
            }
            Optional<Employee> byPhone = employeeRepository.findByPhone(phone);
            if (byPhone.isPresent() && byPhone.get().getId() != employee.getId()){
                return new ControllerResponse("Bu telefon allaqachon ro'yhatdan o'tgan", 301);
            }
            employee.setFirstName(name);
            employee.setLastName(surname);
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setScience(Science.valueOf(science));
            employee.setGender(gender);
            employee.setJoiningDate(joiningDate);
            employee.setBirthDate(birthdate);
            employee.setRole(Role.valueOf(role.toUpperCase()));
            employee.setGrade(grade);
            if(!image.isEmpty()) {
                File uploadFolder = new File(String.format("%s/person_images/",
                        this.uploadFolder));
                if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
                }
                PersonImage image1 = employee.getImage();
                image1.setName(image.getOriginalFilename());
                image1.setContentType(image.getContentType());
                image1.setFileSize(image.getSize());
                image1.setExtension(getExtension(image.getOriginalFilename()));
                image1.setHashId(UUID.randomUUID().toString().substring(0, 10));
                image1.setUploadPath(String.format("person_images/%s.%s",
                        image1.getHashId(),
                        image1.getExtension()));
                PersonImage personImage1 = personImageRepository.save(image1);
                uploadFolder = uploadFolder.getAbsoluteFile();
                File file = new File(uploadFolder, String.format("%s.%s",
                        personImage1.getHashId(),
                        personImage1.getExtension()));
                if (employee.getImage() != null) {
                    PersonImage personImage = personImageRepository.findById(employee.getImage().getId()).get();
                    File file1 = new File(String.format("%s/%s",
                            this.uploadFolder,
                            personImage.getUploadPath()));
                    file1.delete(); // delete old image from folder
                }
                try {
                    image.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                employee.setImage(personImage1);
            }
            if (pass.length() != 0){
                employee.setPassword(pass);
            }
            else {
                employee.setPassword(employee.getPassword());
            }
            employeeRepository.save(employee);
            return new ControllerResponse("Hodim muvaffaqqiyatli tahrirlandi.", 200);
        }
        return new ControllerResponse("Hodim topilmadi.", 301);
    }

    @Override
    public ControllerResponse login(LoginDto loginDto) {
        try {
//            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginDto.getPhone(),
//                    loginDto.getPassword()
//            ));
//            Employee employee = (Employee) authenticate.getPrincipal();
//            String token = jwtProvider.generateToken(employee.getPhone());
//            String path = null;
//            switch (employee.getRole()){
//                case ADMIN -> path = "/api/admin/dashboard";
//                case TEACHER -> path = "/api/teacher/dashboard";
//            }
            return new ControllerResponse("path", 200, "token");
        } catch (Exception e) {
            return new ControllerResponse("Login yoki parol xato", 401);
        }
    }

    @Override
    public ControllerResponse updateAdmin(String name, String surname, String email, String phone, Date birthdate,
                                          String gender, String pass, MultipartFile image) {
        Optional<Employee> employeeOptional = employeeRepository.findByPhone("+998901234567");
        if (employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            Optional<Employee> byEmail = employeeRepository.findByEmail(email);
            if (byEmail.isPresent() && byEmail.get().getId() != employee.getId()){
                return new ControllerResponse("Bu email allaqachon ro'yhatdan o'tgan", 301);
            }
            Optional<Employee> byPhone = employeeRepository.findByPhone(phone);
            if (byPhone.isPresent() && byPhone.get().getId() != employee.getId()){
                return new ControllerResponse("Bu telefon allaqachon ro'yhatdan o'tgan", 301);
            }
            employee.setFirstName(name);
            employee.setLastName(surname);
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setGender(gender);
            employee.setBirthDate(birthdate);
            if (!image.isEmpty()) {
                File uploadFolder = new File(String.format("%s/person_images/",
                        this.uploadFolder));
                if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
                }
                PersonImage image1 = employee.getImage();
                image1.setName(image.getOriginalFilename());
                image1.setContentType(image.getContentType());
                image1.setFileSize(image.getSize());
                image1.setExtension(getExtension(image.getOriginalFilename()));
                image1.setHashId(UUID.randomUUID().toString().substring(0, 10));
                image1.setUploadPath(String.format("person_images/%s.%s",
                        image1.getHashId(),
                        image1.getExtension()));
                PersonImage personImage1 = personImageRepository.save(image1);
                uploadFolder = uploadFolder.getAbsoluteFile();
                File file = new File(uploadFolder, String.format("%s.%s",
                        personImage1.getHashId(),
                        personImage1.getExtension()));
                if (employee.getImage() != null) {
                    PersonImage personImage = personImageRepository.findById(employee.getImage().getId()).get();
                    File file1 = new File(String.format("%s/%s",
                            this.uploadFolder,
                            personImage.getUploadPath()));
                    file1.delete(); // delete old image from folder
                }
                try {
                    image.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                employee.setImage(personImage1);
            }
            if (pass.length() != 0){
                employee.setPassword(pass);
            }
            else {
                employee.setPassword(employee.getPassword());
            }
            employeeRepository.save(employee);
            return new ControllerResponse("Ma'lumotlar muvaffaqqiyatli tahrirlandi.", 200);
        }
        return new ControllerResponse("Profile topilmadi.", 301);
    }

    @Override
    public ControllerResponse addPost(String title, String description, String type, MultipartFile image) {
        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setType(type);
        if (!image.isEmpty()) {
            File uploadFolder = new File(String.format("%s/POST_IMAGES/",
                    this.uploadFolder));
            if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
            }
            post.setContentType(image.getContentType());
            post.setName(image.getOriginalFilename());
            post.setExtension(getExtension(image.getOriginalFilename()));
            post.setFileSize(image.getSize());
            post.setHashId(UUID.randomUUID().toString().substring(0, 10));
            post.setUploadPath(String.format("POST_IMAGES/%s.%s",
                    post.getHashId(),
                    post.getExtension()));
//            PersonImage personImage1 = personImageRepository.save(post);
            uploadFolder = uploadFolder.getAbsoluteFile();
            File file = new File(uploadFolder, String.format("%s.%s",
                    post.getHashId(),
                    post.getExtension()));
            try {
                image.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return new ControllerResponse("Post uchun rasm yuklang", 301);
        }
        postRepository.save(post);
        return new ControllerResponse("Post saqlandi", 200);
    }

    @Override
    public Post getPostImage(String hashId) {
        return postRepository.findByHashId(hashId);
    }

    @Override
    public ControllerResponse sendHomeMsg(String name, String phone, String message) {
        homeMessageRepository.save(new HomeMessage(name, phone, message, true));
        return new ControllerResponse("Xabar jo'natildi", 200);
    }

    @Override
    public ControllerResponse deleteMsg(String id) {
        final Optional<HomeMessage> messageOptional = homeMessageRepository.findById(Integer.valueOf(id));
        if (messageOptional.isPresent()){
            homeMessageRepository.deleteById(Integer.valueOf(id));
            return new ControllerResponse("Xabar o'chirildi", 200);
        }
        return new ControllerResponse("Xabar topilmadi", 208);
    }

    @Override
    public ControllerResponse updatePost(Integer id, String title, String description, String type, MultipartFile image) {
        final Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()){
            final Post post = postOptional.get();
            post.setTitle(title);
            post.setDescription(description);
            post.setType(type);
            if (!image.isEmpty()){
                File uploadFolder = new File(String.format("%s/POST_IMAGES/",
                        this.uploadFolder));
                if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
//                System.out.println("Created folders.");
                }
                File file1 = new File(String.format("%s/%s",
                        this.uploadFolder,
                        post.getUploadPath()));
                file1.delete();
                post.setContentType(image.getContentType());
                post.setName(image.getOriginalFilename());
                post.setExtension(getExtension(image.getOriginalFilename()));
                post.setFileSize(image.getSize());
                post.setHashId(UUID.randomUUID().toString().substring(0, 10));
                post.setUploadPath(String.format("POST_IMAGES/%s.%s",
                        post.getHashId(),
                        post.getExtension()));
//            PersonImage personImage1 = personImageRepository.save(post);
                uploadFolder = uploadFolder.getAbsoluteFile();
                File file = new File(uploadFolder, String.format("%s.%s",
                        post.getHashId(),
                        post.getExtension()));
                try {
                    image.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            postRepository.save(post);
            return new ControllerResponse("Post tahrirlandi", 200);
        }
        return new ControllerResponse("Post topilmadi", 208);
    }

    @Override
    public void deletePost(Integer id) {
        final Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()){
            final Post post = postOptional.get();
            File file = new File(uploadFolder + "/" + post.getUploadPath());
            file.delete();
            postRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public ControllerResponse addTest(String question, MultipartFile questionImg, String ans1, MultipartFile ans1Img,
                                      String ans2, MultipartFile ans2Img, String ans3, MultipartFile ans3Img) {
        final Optional<Lesson> lessonOptional = lessonRepository.findByLessonType(LessonType.IQ);
        if (lessonOptional.isPresent()) {
            final Lesson lesson = lessonOptional.get();
            Test test = new Test();
            test.setLesson(lesson);
            test.setQuestionTxt(question);
            test.setAnswer1(ans1);
            test.setAnswer2(ans2);
            test.setAnswer3(ans3);
            if (!questionImg.isEmpty())
                test.setQuestionImg(getSource(questionImg));
            if (!ans1Img.isEmpty())
                test.setAnswer1Img(getSource(ans1Img));
            if (!ans2Img.isEmpty())
                test.setAnswer2Img(getSource(ans2Img));
            if (!ans3Img.isEmpty())
                test.setAnswer3Img(getSource(ans3Img));
            testRepository.save(test);
            return new ControllerResponse("Test yaratildi", 200);
        }
        return new ControllerResponse("Test ma'lumotlari topilmadi", 208);
    }

    private TestImageSource getSource(MultipartFile file) {
        File uploadFolder = new File(String.format("%s/IQ/", this.uploadFolder));
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            System.out.println(uploadFolder + " path created for test");
        }
        TestImageSource testImageSource = new TestImageSource();
        testImageSource.setContentType(file.getContentType());
        testImageSource.setName(file.getOriginalFilename());
        testImageSource.setExtension(generalService.getExtension(file.getOriginalFilename()));
        testImageSource.setFileSize(file.getSize());
        testImageSource.setHashId(UUID.randomUUID().toString().substring(0, 10));
        testImageSource.setUploadPath(String.format("IQ/%s.%s", testImageSource.getHashId(), testImageSource.getExtension()));
        TestImageSource save = testImgSourceRepository.save(testImageSource);

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file1 = new File(uploadFolder, String.format("%s.%s", testImageSource.getHashId(), testImageSource.getExtension()));
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return save;
    }

    @Override
    public void exportEmployeeToExcel(HttpServletResponse response) throws IOException {
        final List<Employee> employeeList = employeeRepository.findAll();
        exportToExcel(employeeList, response);
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

    public void exportToExcel(List<Employee> employeeList, HttpServletResponse response) throws IOException {
        workbook = new XSSFWorkbook();
        setResponseHeader(response, "application/octet-stream", ".xlsx", "Hodimlar_");
        writeHeaderLine();
        writeDataLine(employeeList);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }

    private void writeDataLine(List<Employee> employeeList) {
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
        for (Employee employee : employeeList) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            createCell(row, columnIndex++, rowIndex - 1, cellStyle);

            createCell(row, columnIndex++, "", cellStyle);

            createCell(row, columnIndex++, employee.getLastName() + " " + employee.getFirstName(), cellStyle);
            createCell(row, columnIndex++, employee.getPhone(), cellStyle);
            createCell(row, columnIndex++, employee.getEmail(), cellStyle);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            createCell(row, columnIndex++, dateFormat.format(employee.getBirthDate()), cellStyle);
            String gender = switch (employee.getGender()) {
                case "Male" -> "Erkak";
                case "Female" -> "Ayol";
                default -> "Aniqlanmadi";
            };
            createCell(row, columnIndex++, gender, cellStyle);
            createCell(row, columnIndex++, employee.getScience().toString(), cellStyle);
            createCell(row, columnIndex++, employee.getGrade(), cellStyle);
            createCell(row, columnIndex++, employee.getRole().toString(), cellStyle);
            createCell(row, columnIndex++, dateFormat.format(employee.getJoiningDate()), cellStyle);
        }
    }

    private void writeHeaderLine() {
        sheet=workbook.createSheet("Hodimlar");
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
        createCell(row, 1, "Rasm", cellStyle);
        createCell(row, 2, "Fio", cellStyle);
        createCell(row, 3, "Telefon", cellStyle);
        createCell(row, 4, "Email", cellStyle);
        createCell(row, 5, "Tug'ilgan kun", cellStyle);
        createCell(row, 6, "Jins", cellStyle);
        createCell(row, 7, "Fan", cellStyle);
        createCell(row, 8, "Sinf", cellStyle);
        createCell(row, 9, "Lavozim", cellStyle);
        createCell(row, 10, "Qo'shilgan vaqt", cellStyle);
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
    public PersonImage getImage(String hashId) {
        return personImageRepository.findByHashId(hashId);
    }
}