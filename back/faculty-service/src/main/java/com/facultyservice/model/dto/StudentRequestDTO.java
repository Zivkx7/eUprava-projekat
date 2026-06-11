package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class StudentRequestDTO {
    private String indexNo;
    private String name;
    private String email;
    private String status;
    private String password;
    private String programId;
}