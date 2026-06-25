package com.facultyservice.controller;

import com.facultyservice.model.dto.ExamRegistrationRequestDTO;
import com.facultyservice.model.dto.ExamRegistrationResponseDTO;
import com.facultyservice.service.ExamRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exam-registrations")
@RequiredArgsConstructor
public class ExamRegistrationController {

    private final ExamRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ExamRegistrationResponseDTO> register(@RequestBody ExamRegistrationRequestDTO dto) {
        return ResponseEntity.ok(registrationService.register(dto));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamRegistrationResponseDTO>> getByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(registrationService.getByStudent(studentId));
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamRegistrationResponseDTO>> getByExam(@PathVariable String examId) {
        return ResponseEntity.ok(registrationService.getByExam(examId));
    }
    @PutMapping("/{id}/grade")
    public ResponseEntity<ExamRegistrationResponseDTO> grade(@PathVariable String id, @RequestParam Integer grade) {
        return ResponseEntity.ok(registrationService.gradeRegistration(id, grade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.noContent().build();
    }
}