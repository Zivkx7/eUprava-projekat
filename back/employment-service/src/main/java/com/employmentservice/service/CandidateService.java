package com.employmentservice.service;

import com.employmentservice.model.dto.CandidateRequestDTO;
import com.employmentservice.model.dto.CandidateResponseDTO;
import com.employmentservice.model.dto.CvResponseDTO;
import java.util.List;

public interface CandidateService {
    CandidateResponseDTO createCandidate(CandidateRequestDTO dto);
    CandidateResponseDTO getCandidateById(String id);
    CandidateResponseDTO getCandidateByEmail(String email);
    List<CandidateResponseDTO> getAllCandidates();
    List<CandidateResponseDTO> searchCandidates(String name);
    CandidateResponseDTO updateCandidate(String id, CandidateRequestDTO dto);
    void deleteCandidate(String id);
    CvResponseDTO generateCV(String candidateId);
}
