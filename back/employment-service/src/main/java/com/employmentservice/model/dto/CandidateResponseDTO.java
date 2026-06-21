package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class CandidateResponseDTO {
    private String id;
    private String fullName;
    private String email;
    private String createdAt;
}
