package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class EmployerResponseDTO {
    private String id;
    private String name;
    private String sector;
    private String email;
    private String createdAt;
}
