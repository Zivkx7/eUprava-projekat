package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class JobOfferRequestDTO {
    private String title;
    private String description;
    private String location;
    private String employerId;
}
