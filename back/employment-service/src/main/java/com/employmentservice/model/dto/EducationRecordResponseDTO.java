package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class EducationRecordResponseDTO {
    private String id;
    private String candidateId;
    private String candidateName;
    private String facultyId;
    private String facultyName;
    private String programId;
    private String programName;
    private String studentId;
    private String degree;
    private String startDate;
    private String endDate;
    private boolean graduated;
    private String graduationDate;
    private Double avgGradeSnapshot;
    private boolean verified;
}
