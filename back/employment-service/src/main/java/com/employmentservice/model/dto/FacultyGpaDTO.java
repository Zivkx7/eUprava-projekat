package com.employmentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Odgovor sa Fakulteta: GET /internal/gpa/{studentId}
 * { studentId, indexNo, name, officialGPA }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacultyGpaDTO {
    private String studentId;
    private String indexNo;
    private String name;
    private Double officialGPA;
}
