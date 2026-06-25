package com.employmentservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fair_check_ins")
public class FairCheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "candidate_id", nullable = false)
    private String candidateId;

    @Column(name = "check_in_time")
    private LocalDateTime time;

    // Jedinstveni token za identifikaciju na sajmu (sadržaj QR koda)
    @Column(nullable = false, unique = true)
    private String qrCode;
}
