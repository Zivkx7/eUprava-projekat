package com.employmentservice.repository;

import com.employmentservice.model.EducationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRecordRepository extends JpaRepository<EducationRecord, String> {
    List<EducationRecord> findByCandidateId(String candidateId);
}
