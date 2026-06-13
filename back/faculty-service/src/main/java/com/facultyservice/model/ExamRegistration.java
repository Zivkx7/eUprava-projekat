package com.facultyservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "exam_registrations")
public class ExamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private String status; // REGISTERED, PASSED, FAILED

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}