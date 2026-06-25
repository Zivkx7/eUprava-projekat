package com.employmentservice.model.dto;

import lombok.Data;

@Data
public class FairCheckInResponseDTO {
    private String id;
    private String candidateId;
    private String candidateName;
    private String time;
    private String qrCode;          // tekstualni token
    private String qrImageBase64;   // PNG data-URL za prikaz QR koda
}
