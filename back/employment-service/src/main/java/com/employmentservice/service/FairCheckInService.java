package com.employmentservice.service;

import com.employmentservice.model.dto.FairCheckInRequestDTO;
import com.employmentservice.model.dto.FairCheckInResponseDTO;
import java.util.List;

public interface FairCheckInService {
    FairCheckInResponseDTO checkIn(FairCheckInRequestDTO dto);
    FairCheckInResponseDTO getById(String id);
    List<FairCheckInResponseDTO> getAll();
    List<FairCheckInResponseDTO> getByCandidate(String candidateId);
    void deleteCheckIn(String id);
}
