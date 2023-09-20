package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDto {
    private int id;
    private String fullName;
    private String phone;
    private String message;
    private boolean status;
    private String date;
}
