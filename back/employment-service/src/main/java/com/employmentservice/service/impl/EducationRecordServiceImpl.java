package com.employmentservice.service.impl;

import com.employmentservice.model.Candidate;
import com.employmentservice.model.EducationRecord;
import com.employmentservice.model.dto.EducationRecordRequestDTO;
import com.employmentservice.model.dto.EducationRecordResponseDTO;
import com.employmentservice.repository.CandidateRepository;
import com.employmentservice.repository.EducationRecordRepository;
import com.employmentservice.service.EducationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationRecordServiceImpl implements EducationRecordService {

    private final EducationRecordRepository educationRecordRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public EducationRecordResponseDTO createEducationRecord(EducationRecordRequestDTO dto) {
        EducationRecord record = new EducationRecord();
        applyDto(record, dto);
        return mapToDTO(educationRecordRepository.save(record));
    }

    @Override
    public EducationRecordResponseDTO getEducationRecordById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public List<EducationRecordResponseDTO> getAllEducationRecords() {
        return educationRecordRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EducationRecordResponseDTO> getByCandidate(String candidateId) {
        return educationRecordRepository.findByCandidateId(candidateId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EducationRecordResponseDTO updateEducationRecord(String id, EducationRecordRequestDTO dto) {
        EducationRecord record = findEntity(id);
        applyDto(record, dto);
        return mapToDTO(educationRecordRepository.save(record));
    }

    @Override
    public void deleteEducationRecord(String id) {
        educationRecordRepository.deleteById(id);
    }

    private void applyDto(EducationRecord record, EducationRecordRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        record.setCandidate(candidate);
        record.setFacultyId(dto.getFacultyId());
        record.setFacultyName(dto.getFacultyName());
        record.setProgramId(dto.getProgramId());
        record.setProgramName(dto.getProgramName());
        record.setStudentId(dto.getStudentId());
        record.setDegree(dto.getDegree());
        record.setStartDate(parseDate(dto.getStartDate()));
        record.setEndDate(parseDate(dto.getEndDate()));
        record.setGraduated(dto.isGraduated());
        record.setGraduationDate(parseDate(dto.getGraduationDate()));
        record.setAvgGradeSnapshot(dto.getAvgGradeSnapshot());
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private EducationRecord findEntity(String id) {
        return educationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Education record not found"));
    }

    private EducationRecordResponseDTO mapToDTO(EducationRecord record) {
        EducationRecordResponseDTO dto = new EducationRecordResponseDTO();
        dto.setId(record.getId());
        if (record.getCandidate() != null) {
            dto.setCandidateId(record.getCandidate().getId());
            dto.setCandidateName(record.getCandidate().getFullName());
        }
        dto.setFacultyId(record.getFacultyId());
        dto.setFacultyName(record.getFacultyName());
        dto.setProgramId(record.getProgramId());
        dto.setProgramName(record.getProgramName());
        dto.setStudentId(record.getStudentId());
        dto.setDegree(record.getDegree());
        dto.setStartDate(record.getStartDate() != null ? record.getStartDate().toString() : null);
        dto.setEndDate(record.getEndDate() != null ? record.getEndDate().toString() : null);
        dto.setGraduated(record.isGraduated());
        dto.setGraduationDate(record.getGraduationDate() != null ? record.getGraduationDate().toString() : null);
        dto.setAvgGradeSnapshot(record.getAvgGradeSnapshot());
        dto.setVerified(record.isVerified());
        return dto;
    }
}
