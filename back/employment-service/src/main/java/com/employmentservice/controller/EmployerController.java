package com.employmentservice.controller;

import com.employmentservice.model.dto.EmployerRequestDTO;
import com.employmentservice.model.dto.EmployerResponseDTO;
import com.employmentservice.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping
    public ResponseEntity<EmployerResponseDTO> create(@RequestBody EmployerRequestDTO dto) {
        return ResponseEntity.ok(employerService.createEmployer(dto));
    }

    @GetMapping
    public ResponseEntity<List<EmployerResponseDTO>> getAll() {
        return ResponseEntity.ok(employerService.getAllEmployers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployerResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(employerService.getEmployerById(id));
    }

    @GetMapping("/by-email")
    public ResponseEntity<EmployerResponseDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employerService.getEmployerByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployerResponseDTO> update(@PathVariable String id, @RequestBody EmployerRequestDTO dto) {
        return ResponseEntity.ok(employerService.updateEmployer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }
}
