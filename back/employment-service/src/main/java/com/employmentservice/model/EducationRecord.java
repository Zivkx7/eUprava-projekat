package com.employmentservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "education_records")
public class EducationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // --- Podaci koje unosi kandidat (ključ za verifikaciju kod Fakulteta) ---
    @Column(name = "index_no")
    private String indexNo;          // broj indeksa studenta

    @Column(name = "student_email")
    private String studentEmail;     // studentski mejl

    @Column(name = "start_date")
    private LocalDate startDate;      // opciono — period studija

    @Column(name = "end_date")
    private LocalDate endDate;        // opciono

    // Sistem radi sa jednim fakultetom (FTN, Novi Sad) — naziv je konstanta/labela.
    @Column(name = "faculty_name")
    private String facultyName;

    // --- Polja koja popunjava Fakultet pri verifikaciji (zvanični podaci) ---
    @Column(name = "faculty_student_id")
    private String facultyStudentId; // interni UUID studenta na Fakultetu

    @Column(name = "program_name")
    private String programName;

    private String degree;           // BSc, MSc, PhD

    private boolean graduated;

    @Column(name = "avg_grade_snapshot")
    private Double avgGradeSnapshot;  // zvanični prosek (GPA) sa Fakulteta

    // Rezultat verifikacije obrazovanja sa Fakultetom
    private boolean verified;
}
