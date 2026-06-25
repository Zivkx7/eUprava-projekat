package com.employmentservice.service;

import com.employmentservice.model.dto.ApplicationRequestDTO;
import com.employmentservice.model.dto.ApplicationResponseDTO;
import java.util.List;

public interface ApplicationService {
    ApplicationResponseDTO apply(ApplicationRequestDTO dto);
    void withdraw(String id);
    ApplicationResponseDTO getById(String id);
    List<ApplicationResponseDTO> getAll();
    List<ApplicationResponseDTO> getByCandidate(String candidateId);
    List<ApplicationResponseDTO> getByJobOffer(String jobOfferId);
    List<ApplicationResponseDTO> getByStatus(String status);
    ApplicationResponseDTO updateStatus(String id, String status);
}
