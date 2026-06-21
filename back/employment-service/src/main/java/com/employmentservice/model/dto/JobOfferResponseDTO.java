package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class JobOfferResponseDTO {
    private String id;
    private String title;
    private String description;
    private String location;
    private String employerId;
    private String employerName;
    private String createdAt;
}
