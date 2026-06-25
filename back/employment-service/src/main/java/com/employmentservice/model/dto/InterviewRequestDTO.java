package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class InterviewRequestDTO {
    private String applicationId;
    private String dateTime;   // ISO format: yyyy-MM-ddTHH:mm:ss
    private String location;
    private String type;       // online, in-person, itd.
}
