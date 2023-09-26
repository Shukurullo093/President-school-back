package com.example.president_school.service;

import com.example.president_school.entity.Student;
import com.example.president_school.payload.EmployeeDto;
import com.example.president_school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GeneralService {
    private final StudentRepository studentRepository;

    public EmployeeDto getProfile(String phone){
        Optional<Student> byPhone = studentRepository.findByPhone(phone);
        EmployeeDto employeeDto = new EmployeeDto();
        if (byPhone.isPresent()) {
            Student student = byPhone.get();
            employeeDto.setFullName(student.getFullName());
            employeeDto.setGrade(String.valueOf(student.getGrade()));
            if (student.getImage() != null) {
                employeeDto.setImageHashId("/api/admin/rest/viewImage/" + student.getImage().getHashId());
            }
            employeeDto.setImageHashId("/images/profile/education/pic-6.jpg");
        }
        return employeeDto;
    }

    public String getExtension(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }

    public String getDateFormat(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public boolean after(Date date1, Date date2){
        final String[] split1 = getDateFormat(date1, "dd MM yyyy").split(" ");
        final String[] split2 = getDateFormat(date2, "dd MM yyyy").split(" ");

        if (Integer.parseInt(split1[2]) <= Integer.parseInt(split2[2]))
        {
            if (Integer.parseInt(split1[1]) <= Integer.parseInt(split2[1]))
            {
                return Integer.parseInt(split1[0]) < Integer.parseInt(split2[0]);
            }
        }
        return false;
    }
}
