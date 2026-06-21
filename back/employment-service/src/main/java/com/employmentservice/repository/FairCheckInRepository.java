package com.employmentservice.repository;

import com.employmentservice.model.FairCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FairCheckInRepository extends JpaRepository<FairCheckIn, String> {
    List<FairCheckIn> findByCandidateId(String candidateId);
}
