package com.facultyservice.model.dto;

import lombok.Data;

@Data
public class ProgramRequestDTO {
    private String facultyId;
    private String name;
    private String degree;
}