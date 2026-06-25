package com.employmentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Jedan red rang liste kandidata (RecommendationEngine).
 */
@Data
@AllArgsConstructor
public class RankedCandidateDTO {
    private int rank;
    private String candidateId;
    private String fullName;
    private String email;
    private Double officialGPA; // zvanični GPA preuzet sa Fakulteta (null ako nije dostupan)
}
