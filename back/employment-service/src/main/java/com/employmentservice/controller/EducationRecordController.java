package com.employmentservice.controller;

import com.employmentservice.model.dto.EducationRecordRequestDTO;
import com.employmentservice.model.dto.EducationRecordResponseDTO;
import com.employmentservice.service.EducationRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/education-records")
@RequiredArgsConstructor
public class EducationRecordController {

    private final EducationRecordService educationRecordService;

    @PostMapping
    public ResponseEntity<EducationRecordResponseDTO> create(@RequestBody EducationRecordRequestDTO dto) {
        return ResponseEntity.ok(educationRecordService.createEducationRecord(dto));
    }

    @GetMapping
    public ResponseEntity<List<EducationRecordResponseDTO>> getAll() {
        return ResponseEntity.ok(educationRecordService.getAllEducationRecords());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationRecordResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(educationRecordService.getEducationRecordById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<EducationRecordResponseDTO>> getByCandidate(@PathVariable String candidateId) {
        return ResponseEntity.ok(educationRecordService.getByCandidate(candidateId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationRecordResponseDTO> update(@PathVariable String id, @RequestBody EducationRecordRequestDTO dto) {
        return ResponseEntity.ok(educationRecordService.updateEducationRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        educationRecordService.deleteEducationRecord(id);
        return ResponseEntity.noContent().build();
    }
}
