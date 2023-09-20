package com.example.president_school.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private int id;
    private String title;
    private String description;
    private String type;
    private String date;
    private String imagePath;
}
