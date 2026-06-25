package com.employmentservice.controller;

import com.employmentservice.model.dto.JobOfferRequestDTO;
import com.employmentservice.model.dto.JobOfferResponseDTO;
import com.employmentservice.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
@RequiredArgsConstructor
public class JobOfferController {

    private final JobOfferService jobOfferService;

    @PostMapping
    public ResponseEntity<JobOfferResponseDTO> create(@RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.ok(jobOfferService.createJobOffer(dto));
    }

    @GetMapping
    public ResponseEntity<List<JobOfferResponseDTO>> getAll() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(jobOfferService.getJobOfferById(id));
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<JobOfferResponseDTO>> getByEmployer(@PathVariable String employerId) {
        return ResponseEntity.ok(jobOfferService.getJobOffersByEmployer(employerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobOfferResponseDTO>> search(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(jobOfferService.searchJobOffers(q));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOfferResponseDTO> update(@PathVariable String id, @RequestBody JobOfferRequestDTO dto) {
        return ResponseEntity.ok(jobOfferService.updateJobOffer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}
