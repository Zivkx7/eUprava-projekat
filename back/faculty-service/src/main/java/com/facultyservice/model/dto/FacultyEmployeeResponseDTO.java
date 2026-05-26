package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class FacultyEmployeeResponseDTO {
    private String id;
    private String facultyId;
    private String fullName;
    private String role;
    private String email;
}
