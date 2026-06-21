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

    @Column(name = "faculty_id")
    private String facultyId;        // referenca na fakultet (UUID iz Fakulteta)

    @Column(name = "faculty_name")
    private String facultyName;

    @Column(name = "program_id")
    private String programId;        // referenca na studijski program (UUID iz Fakulteta)

    @Column(name = "program_name")
    private String programName;

    // Referenca na studenta u mikroservisu Fakultet — koristi se za verifikaciju
    // obrazovanja i preuzimanje zvaničnog GPA (FacultyAPI.getOfficialGPA).
    @Column(name = "student_id")
    private String studentId;

    private String degree;           // BSc, MSc, PhD, itd.

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private boolean graduated;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    // Lokalni snimak proseka (zvanično ide preko FacultyAPI)
    @Column(name = "avg_grade_snapshot")
    private Double avgGradeSnapshot;

    // Rezultat verifikacije obrazovanja sa Fakultetom
    private boolean verified;
}
