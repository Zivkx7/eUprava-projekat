package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private String id;
    private String indexNo;
    private String name;
    private String email;
    private String status;
    private String createdAt;
}