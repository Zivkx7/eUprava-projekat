package com.employmentservice.service.impl;

import com.employmentservice.client.FacultyClient;
import com.employmentservice.model.Candidate;
import com.employmentservice.model.EducationRecord;
import com.employmentservice.model.dto.EducationRecordRequestDTO;
import com.employmentservice.model.dto.EducationRecordResponseDTO;
import com.employmentservice.model.dto.StudentVerificationDTO;
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

    // Sistem radi sa jednim fakultetom — naziv je fiksna labela.
    private static final String FACULTY_NAME = "Fakultet tehničkih nauka, Novi Sad";

    private final EducationRecordRepository educationRecordRepository;
    private final CandidateRepository candidateRepository;
    private final FacultyClient facultyClient;

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

    @Override
    public EducationRecordResponseDTO verify(String id) {
        EducationRecord record = findEntity(id);
        applyVerification(record);
        return mapToDTO(educationRecordRepository.save(record));
    }

    @Override
    public List<EducationRecordResponseDTO> verifyByCandidate(String candidateId) {
        List<EducationRecord> records = educationRecordRepository.findByCandidateId(candidateId);
        records.forEach(this::applyVerification);
        educationRecordRepository.saveAll(records);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Verifikuje zapis kod Fakulteta preko broja indeksa i studentskog mejla.
     * Pri uspehu označava zapis kao verifikovan i popunjava zvanične podatke
     * (program, nivo, status diplomiranja, prosek) iz odgovora Fakulteta.
     */
    private void applyVerification(EducationRecord record) {
        if (isBlank(record.getIndexNo()) || isBlank(record.getStudentEmail())) {
            record.setVerified(false);
            return;
        }
        StudentVerificationDTO v = facultyClient.verifyStudent(record.getIndexNo(), record.getStudentEmail());
        if (v != null && v.isVerified()) {
            record.setVerified(true);
            record.setFacultyStudentId(v.getStudentId());
            record.setProgramName(v.getProgramName());
            record.setDegree(v.getDegree());
            record.setGraduated(v.isGraduated());
            if (v.getOfficialGPA() != null) {
                record.setAvgGradeSnapshot(v.getOfficialGPA());
            }
        } else {
            record.setVerified(false);
        }
    }

    private void applyDto(EducationRecord record, EducationRecordRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        record.setCandidate(candidate);
        record.setIndexNo(dto.getIndexNo());
        record.setStudentEmail(dto.getStudentEmail());
        record.setStartDate(parseDate(dto.getStartDate()));
        record.setEndDate(parseDate(dto.getEndDate()));
        record.setFacultyName(FACULTY_NAME);
        // Izmenom indeksa/mejla zapis prestaje da bude verifikovan dok se ponovo ne proveri.
        record.setVerified(false);
    }

    private LocalDate parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
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
        dto.setIndexNo(record.getIndexNo());
        dto.setStudentEmail(record.getStudentEmail());
        dto.setFacultyName(record.getFacultyName());
        dto.setFacultyStudentId(record.getFacultyStudentId());
        dto.setProgramName(record.getProgramName());
        dto.setDegree(record.getDegree());
        dto.setGraduated(record.isGraduated());
        dto.setStartDate(record.getStartDate() != null ? record.getStartDate().toString() : null);
        dto.setEndDate(record.getEndDate() != null ? record.getEndDate().toString() : null);
        dto.setAvgGradeSnapshot(record.getAvgGradeSnapshot());
        dto.setVerified(record.isVerified());
        return dto;
    }
}
