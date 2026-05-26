package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class ExamResponseDTO {
    private String id;
    private String courseId;
    private String dateTime;
    private String room;
}