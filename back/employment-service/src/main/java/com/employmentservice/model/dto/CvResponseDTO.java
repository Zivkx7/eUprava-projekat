package com.employmentservice.model.dto;

import lombok.Data;
import java.util.List;

/**
 * Generisani CV kandidata — objedinjuje lične podatke i obrazovne zapise.
 */
@Data
public class CvResponseDTO {
    private String candidateId;
    private String fullName;
    private String email;
    private List<EducationRecordResponseDTO> education;
    private String summary; // tekstualni prikaz CV-a
}
