package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class ApplicationResponseDTO {
    private String id;
    private String candidateId;
    private String candidateName;
    private String jobOfferId;
    private String jobOfferTitle;
    private String status;
    private String createdAt;
}
