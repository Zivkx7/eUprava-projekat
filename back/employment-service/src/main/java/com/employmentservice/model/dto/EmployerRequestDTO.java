package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class EmployerRequestDTO {
    private String name;
    private String sector;
    private String email;
    private String password; // za EMPLOYER korisnički nalog (samo pri kreiranju)
}
