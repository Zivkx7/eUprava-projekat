package com.employmentservice.service;

import com.employmentservice.client.FacultyClient;
import com.employmentservice.model.Application;
import com.employmentservice.model.Candidate;
import com.employmentservice.model.EducationRecord;
import com.employmentservice.model.dto.RankedCandidateDTO;
import com.employmentservice.model.dto.StudentVerificationDTO;
import com.employmentservice.repository.ApplicationRepository;
import com.employmentservice.repository.CandidateRepository;
import com.employmentservice.repository.EducationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Rangira kandidate na osnovu zvaničnog GPA pribavljenog od Fakulteta
 * (FacultyAPI.getOfficialGPA preko {@link FacultyClient}).
 */
@Service
@RequiredArgsConstructor
public class RecommendationEngine {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final EducationRecordRepository educationRecordRepository;
    private final FacultyClient facultyClient;

    /** Rangira kandidate koji su se prijavili na datu ponudu. */
    public List<RankedCandidateDTO> rankCandidates(String jobOfferId) {
        Map<String, Candidate> uniqueCandidates = new LinkedHashMap<>();
        for (Application app : applicationRepository.findByJobOfferId(jobOfferId)) {
            if (app.getCandidate() != null) {
                uniqueCandidates.putIfAbsent(app.getCandidate().getId(), app.getCandidate());
            }
        }
        return rank(new ArrayList<>(uniqueCandidates.values()));
    }

    /**
     * Predlaže potencijalne kandidate za ponudu na osnovu GPA i obrazovnog profila
     * (svi kandidati koji imaju obrazovni zapis), rangirane opadajuće.
     */
    public List<RankedCandidateDTO> suggestCandidates(String jobOfferId) {
        return rank(candidateRepository.findAll());
    }

    private List<RankedCandidateDTO> rank(List<Candidate> candidates) {
        List<RankedCandidateDTO> ranked = new ArrayList<>();
        for (Candidate c : candidates) {
            Double gpa = resolveOfficialGpa(c.getId());
            ranked.add(new RankedCandidateDTO(0, c.getId(), c.getFullName(), c.getEmail(), gpa));
        }
        // Opadajuće po GPA; kandidati bez dostupnog GPA idu na kraj
        ranked.sort((a, b) -> {
            Double ga = a.getOfficialGPA();
            Double gb = b.getOfficialGPA();
            if (ga == null && gb == null) return 0;
            if (ga == null) return 1;
            if (gb == null) return -1;
            return gb.compareTo(ga);
        });
        for (int i = 0; i < ranked.size(); i++) {
            ranked.get(i).setRank(i + 1);
        }
        return ranked;
    }

    /**
     * Preuzima zvanični GPA kandidata sa Fakulteta na osnovu njegovih obrazovnih zapisa.
     * Verifikacija ide preko broja indeksa i studentskog mejla.
     */
    private Double resolveOfficialGpa(String candidateId) {
        for (EducationRecord record : educationRecordRepository.findByCandidateId(candidateId)) {
            if (record.getIndexNo() != null && record.getStudentEmail() != null) {
                StudentVerificationDTO v = facultyClient.verifyStudent(
                        record.getIndexNo(), record.getStudentEmail());
                if (v != null && v.isVerified() && v.getOfficialGPA() != null) {
                    return v.getOfficialGPA();
                }
            }
        }
        return null;
    }
}
