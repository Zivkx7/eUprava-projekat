package com.employmentservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String location;

    private String type; // online, in-person, itd.

    @Column(name = "invite_sent")
    private boolean inviteSent;
}
