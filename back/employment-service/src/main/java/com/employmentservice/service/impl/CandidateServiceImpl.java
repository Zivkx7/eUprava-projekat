package com.employmentservice.service.impl;

import com.employmentservice.model.Candidate;
import com.employmentservice.model.Role;
import com.employmentservice.model.User;
import com.employmentservice.model.dto.CandidateRequestDTO;
import com.employmentservice.model.dto.CandidateResponseDTO;
import com.employmentservice.model.dto.CvResponseDTO;
import com.employmentservice.model.dto.EducationRecordResponseDTO;
import com.employmentservice.repository.CandidateRepository;
import com.employmentservice.repository.UserRepository;
import com.employmentservice.service.CandidateService;
import com.employmentservice.service.EducationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EducationRecordService educationRecordService;

    @Override
    public CandidateResponseDTO createCandidate(CandidateRequestDTO dto) {
        Candidate candidate = new Candidate();
        candidate.setFullName(dto.getFullName());
        candidate.setEmail(dto.getEmail());
        candidateRepository.save(candidate);

        // Automatsko kreiranje CANDIDATE korisničkog naloga
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(
                dto.getPassword() != null ? dto.getPassword() : "candidate"));
        user.setRole(Role.CANDIDATE);
        userRepository.save(user);

        return mapToDTO(candidate);
    }

    @Override
    public CandidateResponseDTO getCandidateById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public CandidateResponseDTO getCandidateByEmail(String email) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        return mapToDTO(candidate);
    }

    @Override
    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CandidateResponseDTO> searchCandidates(String name) {
        String q = name == null ? "" : name;
        return candidateRepository.findByFullNameContainingIgnoreCase(q)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CandidateResponseDTO updateCandidate(String id, CandidateRequestDTO dto) {
        Candidate candidate = findEntity(id);
        candidate.setFullName(dto.getFullName());
        candidate.setEmail(dto.getEmail());
        return mapToDTO(candidateRepository.save(candidate));
    }

    @Override
    public void deleteCandidate(String id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public CvResponseDTO generateCV(String candidateId) {
        Candidate candidate = findEntity(candidateId);
        List<EducationRecordResponseDTO> education = educationRecordService.getByCandidate(candidateId);

        StringBuilder sb = new StringBuilder();
        sb.append("CV — ").append(candidate.getFullName()).append("\n");
        sb.append("Email: ").append(candidate.getEmail()).append("\n");
        sb.append("Obrazovanje:\n");
        if (education.isEmpty()) {
            sb.append("  (nema unetih obrazovnih zapisa)\n");
        } else {
            for (EducationRecordResponseDTO e : education) {
                sb.append("  - ").append(e.getDegree() != null ? e.getDegree() : "")
                        .append(", ").append(e.getProgramName() != null ? e.getProgramName() : "")
                        .append(" @ ").append(e.getFacultyName() != null ? e.getFacultyName() : "")
                        .append(e.isGraduated() ? " (diplomirao)" : " (u toku)")
                        .append(e.getAvgGradeSnapshot() != null ? ", prosek: " + e.getAvgGradeSnapshot() : "")
                        .append("\n");
            }
        }

        CvResponseDTO cv = new CvResponseDTO();
        cv.setCandidateId(candidate.getId());
        cv.setFullName(candidate.getFullName());
        cv.setEmail(candidate.getEmail());
        cv.setEducation(education);
        cv.setSummary(sb.toString());
        return cv;
    }

    private Candidate findEntity(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    private CandidateResponseDTO mapToDTO(Candidate candidate) {
        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(candidate.getId());
        dto.setFullName(candidate.getFullName());
        dto.setEmail(candidate.getEmail());
        if (candidate.getCreatedAt() != null) {
            dto.setCreatedAt(candidate.getCreatedAt().toString());
        }
        return dto;
    }
}
