package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class EducationRecordResponseDTO {
    private String id;
    private String candidateId;
    private String candidateName;
    private String indexNo;
    private String studentEmail;
    private String facultyName;
    private String facultyStudentId;
    private String programName;
    private String degree;
    private boolean graduated;
    private String startDate;
    private String endDate;
    private Double avgGradeSnapshot;
    private boolean verified;
}
