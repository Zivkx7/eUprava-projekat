package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class CandidateRequestDTO {
    private String fullName;
    private String email;
    private String password; // za CANDIDATE korisnički nalog (samo pri kreiranju)
}
