package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class ExamRequestDTO {
    private String courseId;
    private String dateTime;
    private String room;
}