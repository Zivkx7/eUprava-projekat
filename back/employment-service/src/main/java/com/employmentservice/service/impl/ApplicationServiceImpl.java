package com.employmentservice.service.impl;

import com.employmentservice.model.Application;
import com.employmentservice.model.Candidate;
import com.employmentservice.model.JobOffer;
import com.employmentservice.model.dto.ApplicationRequestDTO;
import com.employmentservice.model.dto.ApplicationResponseDTO;
import com.employmentservice.repository.ApplicationRepository;
import com.employmentservice.repository.CandidateRepository;
import com.employmentservice.repository.JobOfferRepository;
import com.employmentservice.service.ApplicationService;
import com.employmentservice.service.EducationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobOfferRepository jobOfferRepository;
    private final EducationRecordService educationRecordService;

    @Override
    public ApplicationResponseDTO apply(ApplicationRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobOffer offer = jobOfferRepository.findById(dto.getJobOfferId())
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJobOffer(offer);
        application.setStatus("SUBMITTED");
        Application saved = applicationRepository.save(application);

        // Prijava na ponudu automatski inicira verifikaciju obrazovnih
        // podataka kandidata sa Fakultetom (zavisna funkcija).
        educationRecordService.verifyByCandidate(candidate.getId());

        return mapToDTO(saved);
    }

    @Override
    public void withdraw(String id) {
        Application application = findEntity(id);
        applicationRepository.delete(application);
    }

    @Override
    public ApplicationResponseDTO getById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public List<ApplicationResponseDTO> getAll() {
        return applicationRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDTO> getByCandidate(String candidateId) {
        return applicationRepository.findByCandidateId(candidateId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDTO> getByJobOffer(String jobOfferId) {
        return applicationRepository.findByJobOfferId(jobOfferId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponseDTO> getByStatus(String status) {
        return applicationRepository.findByStatus(status)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponseDTO updateStatus(String id, String status) {
        Application application = findEntity(id);
        application.setStatus(status);
        return mapToDTO(applicationRepository.save(application));
    }

    private Application findEntity(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    private ApplicationResponseDTO mapToDTO(Application application) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(application.getId());
        if (application.getCandidate() != null) {
            dto.setCandidateId(application.getCandidate().getId());
            dto.setCandidateName(application.getCandidate().getFullName());
        }
        if (application.getJobOffer() != null) {
            dto.setJobOfferId(application.getJobOffer().getId());
            dto.setJobOfferTitle(application.getJobOffer().getTitle());
        }
        dto.setStatus(application.getStatus());
        if (application.getCreatedAt() != null) {
            dto.setCreatedAt(application.getCreatedAt().toString());
        }
        return dto;
    }
}
