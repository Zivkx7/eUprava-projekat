package com.employmentservice.repository;

import com.employmentservice.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, String> {
    List<JobOffer> findByEmployerId(String employerId);
    List<JobOffer> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String location);
}
