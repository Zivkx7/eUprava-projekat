package com.employmentservice.service;

import com.employmentservice.model.dto.JobOfferRequestDTO;
import com.employmentservice.model.dto.JobOfferResponseDTO;
import java.util.List;

public interface JobOfferService {
    JobOfferResponseDTO createJobOffer(JobOfferRequestDTO dto);
    JobOfferResponseDTO getJobOfferById(String id);
    List<JobOfferResponseDTO> getAllJobOffers();
    List<JobOfferResponseDTO> getJobOffersByEmployer(String employerId);
    List<JobOfferResponseDTO> searchJobOffers(String query);
    JobOfferResponseDTO updateJobOffer(String id, JobOfferRequestDTO dto);
    void deleteJobOffer(String id);
}
