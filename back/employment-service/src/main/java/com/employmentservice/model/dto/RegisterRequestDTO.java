package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String role; // ADMIN, EMPLOYER, CANDIDATE
}
