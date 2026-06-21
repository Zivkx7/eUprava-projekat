package com.employmentservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Odgovor sa Fakulteta: GET /internal/employees
 * { id, fullName, role, email }
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacultyEmployeeDTO {
    private String id;
    private String fullName;
    private String role;
    private String email;
}
