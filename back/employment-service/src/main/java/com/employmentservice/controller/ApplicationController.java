package com.employmentservice.controller;

import com.employmentservice.model.dto.ApplicationRequestDTO;
import com.employmentservice.model.dto.ApplicationResponseDTO;
import com.employmentservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Kandidat podnosi prijavu (apply)
    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> apply(@RequestBody ApplicationRequestDTO dto) {
        return ResponseEntity.ok(applicationService.apply(dto));
    }

    // Kandidat povlači prijavu (withdraw)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable String id) {
        applicationService.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponseDTO>> getAll() {
        return ResponseEntity.ok(applicationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByCandidate(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.getByCandidate(candidateId));
    }

    @GetMapping("/job-offer/{jobOfferId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByJobOffer(@PathVariable String jobOfferId) {
        return ResponseEntity.ok(applicationService.getByJobOffer(jobOfferId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(applicationService.getByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}
