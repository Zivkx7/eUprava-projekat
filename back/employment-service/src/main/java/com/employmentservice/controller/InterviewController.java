package com.employmentservice.controller;

import com.employmentservice.model.dto.InterviewRequestDTO;
import com.employmentservice.model.dto.InterviewResponseDTO;
import com.employmentservice.model.dto.InviteResponseDTO;
import com.employmentservice.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponseDTO> schedule(@RequestBody InterviewRequestDTO dto) {
        return ResponseEntity.ok(interviewService.scheduleInterview(dto));
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponseDTO>> getAll() {
        return ResponseEntity.ok(interviewService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(interviewService.getById(id));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponseDTO>> getByApplication(@PathVariable String applicationId) {
        return ResponseEntity.ok(interviewService.getByApplication(applicationId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> update(@PathVariable String id, @RequestBody InterviewRequestDTO dto) {
        return ResponseEntity.ok(interviewService.updateInterview(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

    // Slanje pozivnice kandidatu (sendInvite)
    @PostMapping("/{id}/invite")
    public ResponseEntity<InviteResponseDTO> sendInvite(@PathVariable String id) {
        return ResponseEntity.ok(interviewService.sendInvite(id));
    }
}
