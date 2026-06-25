package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class InterviewResponseDTO {
    private String id;
    private String applicationId;
    private String candidateName;
    private String jobOfferTitle;
    private String dateTime;
    private String location;
    private String type;
    private boolean inviteSent;
}
