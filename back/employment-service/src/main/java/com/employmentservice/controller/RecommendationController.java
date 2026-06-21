package com.employmentservice.controller;

import com.employmentservice.client.FacultyClient;
import com.employmentservice.model.dto.FacultyEmployeeDTO;
import com.employmentservice.model.dto.RankedCandidateDTO;
import com.employmentservice.service.RecommendationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationEngine recommendationEngine;
    private final FacultyClient facultyClient;

    // Rang lista prijavljenih kandidata za ponudu (na osnovu zvaničnog GPA)
    @GetMapping
    public ResponseEntity<List<RankedCandidateDTO>> rank(@RequestParam String offerId) {
        return ResponseEntity.ok(recommendationEngine.rankCandidates(offerId));
    }

    // Predlog potencijalnih kandidata za ponudu (svi kandidati, rangirani po GPA)
    @GetMapping("/suggest")
    public ResponseEntity<List<RankedCandidateDTO>> suggest(@RequestParam String offerId) {
        return ResponseEntity.ok(recommendationEngine.suggestCandidates(offerId));
    }

    // Lista zaposlenih sa Fakulteta — verifikacija radnih mesta
    @GetMapping("/faculty-employees")
    public ResponseEntity<List<FacultyEmployeeDTO>> facultyEmployees() {
        return ResponseEntity.ok(facultyClient.listEmployees());
    }
}
