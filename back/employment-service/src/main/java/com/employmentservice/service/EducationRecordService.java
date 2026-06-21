package com.employmentservice.service;

import com.employmentservice.model.dto.EducationRecordRequestDTO;
import com.employmentservice.model.dto.EducationRecordResponseDTO;
import java.util.List;

public interface EducationRecordService {
    EducationRecordResponseDTO createEducationRecord(EducationRecordRequestDTO dto);
    EducationRecordResponseDTO getEducationRecordById(String id);
    List<EducationRecordResponseDTO> getAllEducationRecords();
    List<EducationRecordResponseDTO> getByCandidate(String candidateId);
    EducationRecordResponseDTO updateEducationRecord(String id, EducationRecordRequestDTO dto);
    void deleteEducationRecord(String id);
}
