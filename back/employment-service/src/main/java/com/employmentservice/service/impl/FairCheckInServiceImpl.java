package com.employmentservice.service.impl;

import com.employmentservice.model.Candidate;
import com.employmentservice.model.FairCheckIn;
import com.employmentservice.model.dto.FairCheckInRequestDTO;
import com.employmentservice.model.dto.FairCheckInResponseDTO;
import com.employmentservice.repository.CandidateRepository;
import com.employmentservice.repository.FairCheckInRepository;
import com.employmentservice.service.FairCheckInService;
import com.employmentservice.util.QrCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FairCheckInServiceImpl implements FairCheckInService {

    private final FairCheckInRepository fairCheckInRepository;
    private final CandidateRepository candidateRepository;
    private final QrCodeGenerator qrCodeGenerator;

    @Override
    public FairCheckInResponseDTO checkIn(FairCheckInRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String token = "FAIR-" + candidate.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);

        FairCheckIn checkIn = new FairCheckIn();
        checkIn.setCandidateId(candidate.getId());
        checkIn.setTime(LocalDateTime.now());
        checkIn.setQrCode(token);
        fairCheckInRepository.save(checkIn);

        return mapToDTO(checkIn);
    }

    @Override
    public FairCheckInResponseDTO getById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public List<FairCheckInResponseDTO> getAll() {
        return fairCheckInRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FairCheckInResponseDTO> getByCandidate(String candidateId) {
        return fairCheckInRepository.findByCandidateId(candidateId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCheckIn(String id) {
        fairCheckInRepository.deleteById(id);
    }

    private FairCheckIn findEntity(String id) {
        return fairCheckInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fair check-in not found"));
    }

    private FairCheckInResponseDTO mapToDTO(FairCheckIn checkIn) {
        FairCheckInResponseDTO dto = new FairCheckInResponseDTO();
        dto.setId(checkIn.getId());
        dto.setCandidateId(checkIn.getCandidateId());
        candidateRepository.findById(checkIn.getCandidateId())
                .ifPresent(c -> dto.setCandidateName(c.getFullName()));
        dto.setTime(checkIn.getTime() != null ? checkIn.getTime().toString() : null);
        dto.setQrCode(checkIn.getQrCode());
        dto.setQrImageBase64(qrCodeGenerator.generateBase64(checkIn.getQrCode()));
        return dto;
    }
}
