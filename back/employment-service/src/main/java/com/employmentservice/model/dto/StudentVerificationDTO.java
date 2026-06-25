package com.employmentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Odgovor sa Fakulteta na verifikaciju studenta:
 * POST /internal/students/verify  { indexNo, email }
 *
 * Fakultet potvrđuje studenta ako se poklope broj indeksa I studentski mejl,
 * i vraća zvanične podatke (program, nivo, status, prosek).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentVerificationDTO {
    private boolean verified;
    private String studentId;     // interni UUID studenta na Fakultetu
    private String indexNo;
    private String name;
    private String email;
    private String programName;
    private String degree;        // BSc, MSc, PhD
    private boolean graduated;
    private String status;        // ACTIVE, GRADUATED, SUSPENDED
    private Double officialGPA;
}
