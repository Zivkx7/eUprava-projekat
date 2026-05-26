package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class CourseResponseDTO {
    private String id;
    private String programId;
    private String code;
    private String name;
    private int ects;
}