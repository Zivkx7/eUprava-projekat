package com.employmentservice.repository;

import com.employmentservice.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByJobOfferId(String jobOfferId);
    List<Application> findByStatus(String status);
}
