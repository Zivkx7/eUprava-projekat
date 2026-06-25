package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class FacultyEmployeeRequestDTO {
    private String fullName;
    private String role;
    private String email;
    private String password;
}