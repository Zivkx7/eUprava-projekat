package com.employmentservice.controller;

import com.employmentservice.model.dto.CandidateRequestDTO;
import com.employmentservice.model.dto.CandidateResponseDTO;
import com.employmentservice.model.dto.CvResponseDTO;
import com.employmentservice.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> create(@RequestBody CandidateRequestDTO dto) {
        return ResponseEntity.ok(candidateService.createCandidate(dto));
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAll() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @GetMapping("/by-email")
    public ResponseEntity<CandidateResponseDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(candidateService.getCandidateByEmail(email));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CandidateResponseDTO>> search(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(candidateService.searchCandidates(name));
    }

    @GetMapping("/{id}/cv")
    public ResponseEntity<CvResponseDTO> generateCV(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.generateCV(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> update(@PathVariable String id, @RequestBody CandidateRequestDTO dto) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
