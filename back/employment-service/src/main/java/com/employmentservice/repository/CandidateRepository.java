package com.employmentservice.repository;

import com.employmentservice.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    Optional<Candidate> findByEmail(String email);
    List<Candidate> findByFullNameContainingIgnoreCase(String fullName);
}
