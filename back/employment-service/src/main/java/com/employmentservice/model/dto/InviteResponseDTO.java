package com.employmentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Rezultat slanja pozivnice kandidatu (Interview.sendInvite()).
 */
@Data
@AllArgsConstructor
public class InviteResponseDTO {
    private String interviewId;
    private String message;
    private String sentAt;
}
