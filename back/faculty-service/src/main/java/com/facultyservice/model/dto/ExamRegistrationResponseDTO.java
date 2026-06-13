package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class ExamRegistrationResponseDTO {
    private String id;
    private String studentId;
    private String studentName;
    private String examId;
    private String courseName;
    private String examDateTime;
    private String status;
    private String registeredAt;
}