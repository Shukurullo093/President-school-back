package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerResponse {
    private String message;
    private int statusCode;
    private String object;

    public ControllerResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}