package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Integer id;
    private String fullName;
    private String gender;
    private String phone;
    private String email;
    private String birthDate;
    private String science;
    private String joiningDate;
    private String imageHashId;
    private String role;
    private String grade;

    public EmployeeDto(Integer id, String fullName, String imageHashId) {
        this.id = id;
        this.fullName = fullName;
        this.imageHashId = imageHashId;
    }
}
