package com.employmentservice.controller;

import com.employmentservice.model.dto.FairCheckInRequestDTO;
import com.employmentservice.model.dto.FairCheckInResponseDTO;
import com.employmentservice.service.FairCheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fair-checkins")
@RequiredArgsConstructor
public class FairCheckInController {

    private final FairCheckInService fairCheckInService;

    // Prijava kandidata na sajam zapošljavanja — generiše QR kod
    @PostMapping
    public ResponseEntity<FairCheckInResponseDTO> checkIn(@RequestBody FairCheckInRequestDTO dto) {
        return ResponseEntity.ok(fairCheckInService.checkIn(dto));
    }

    @GetMapping
    public ResponseEntity<List<FairCheckInResponseDTO>> getAll() {
        return ResponseEntity.ok(fairCheckInService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FairCheckInResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(fairCheckInService.getById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<FairCheckInResponseDTO>> getByCandidate(@PathVariable String candidateId) {
        return ResponseEntity.ok(fairCheckInService.getByCandidate(candidateId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        fairCheckInService.deleteCheckIn(id);
        return ResponseEntity.noContent().build();
    }
}
