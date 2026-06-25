package com.employmentservice.model.dto;

import lombok.Data;

/**
 * Kandidat unosi samo broj indeksa i studentski mejl (+ opciono period studija).
 * Zvanične podatke (program, nivo, status, prosek) popunjava Fakultet pri verifikaciji.
 */
@Data
public class EducationRecordRequestDTO {
    private String candidateId;
    private String indexNo;
    private String studentEmail;
    private String startDate;     // ISO format: yyyy-MM-dd (opciono)
    private String endDate;       // ISO format: yyyy-MM-dd (opciono)
}
