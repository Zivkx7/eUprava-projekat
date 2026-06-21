package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class EducationRecordRequestDTO {
    private String candidateId;
    private String facultyId;
    private String facultyName;
    private String programId;
    private String programName;
    private String studentId;     // ID studenta u mikroservisu Fakultet
    private String degree;
    private String startDate;     // ISO format: yyyy-MM-dd
    private String endDate;       // ISO format: yyyy-MM-dd
    private boolean graduated;
    private String graduationDate; // ISO format: yyyy-MM-dd (opciono)
    private Double avgGradeSnapshot;
}
