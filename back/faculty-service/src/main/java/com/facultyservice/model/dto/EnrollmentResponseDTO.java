package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class EnrollmentResponseDTO {
    private String id;
    private String studentId;
    private String courseId;
    private Integer grade;
    private String enrolledAt;
}
