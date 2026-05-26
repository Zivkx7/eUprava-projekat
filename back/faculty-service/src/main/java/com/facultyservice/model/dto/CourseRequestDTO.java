package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class CourseRequestDTO {
    private String programId;
    private String code;
    private String name;
    private int ects;
}
