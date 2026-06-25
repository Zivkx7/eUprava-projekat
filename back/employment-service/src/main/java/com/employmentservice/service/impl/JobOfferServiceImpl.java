package com.employmentservice.service.impl;

import com.employmentservice.model.Employer;
import com.employmentservice.model.JobOffer;
import com.employmentservice.model.dto.JobOfferRequestDTO;
import com.employmentservice.model.dto.JobOfferResponseDTO;
import com.employmentservice.repository.EmployerRepository;
import com.employmentservice.repository.JobOfferRepository;
import com.employmentservice.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final EmployerRepository employerRepository;

    @Override
    public JobOfferResponseDTO createJobOffer(JobOfferRequestDTO dto) {
        JobOffer offer = new JobOffer();
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setLocation(dto.getLocation());
        offer.setEmployer(findEmployer(dto.getEmployerId()));
        return mapToDTO(jobOfferRepository.save(offer));
    }

    @Override
    public JobOfferResponseDTO getJobOfferById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public List<JobOfferResponseDTO> getAllJobOffers() {
        return jobOfferRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobOfferResponseDTO> getJobOffersByEmployer(String employerId) {
        return jobOfferRepository.findByEmployerId(employerId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobOfferResponseDTO> searchJobOffers(String query) {
        String q = query == null ? "" : query;
        return jobOfferRepository
                .findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(q, q)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobOfferResponseDTO updateJobOffer(String id, JobOfferRequestDTO dto) {
        JobOffer offer = findEntity(id);
        offer.setTitle(dto.getTitle());
        offer.setDescription(dto.getDescription());
        offer.setLocation(dto.getLocation());
        if (dto.getEmployerId() != null) {
            offer.setEmployer(findEmployer(dto.getEmployerId()));
        }
        return mapToDTO(jobOfferRepository.save(offer));
    }

    @Override
    public void deleteJobOffer(String id) {
        jobOfferRepository.deleteById(id);
    }

    private JobOffer findEntity(String id) {
        return jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));
    }

    private Employer findEmployer(String employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    private JobOfferResponseDTO mapToDTO(JobOffer offer) {
        JobOfferResponseDTO dto = new JobOfferResponseDTO();
        dto.setId(offer.getId());
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        dto.setLocation(offer.getLocation());
        if (offer.getEmployer() != null) {
            dto.setEmployerId(offer.getEmployer().getId());
            dto.setEmployerName(offer.getEmployer().getName());
        }
        if (offer.getCreatedAt() != null) {
            dto.setCreatedAt(offer.getCreatedAt().toString());
        }
        return dto;
    }
}
