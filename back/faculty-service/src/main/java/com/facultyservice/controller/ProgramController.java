package com.facultyservice.controller;

import com.facultyservice.model.dto.ProgramRequestDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import com.facultyservice.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<ProgramResponseDTO> createProgram(@RequestBody ProgramRequestDTO dto) {
        return ResponseEntity.ok(programService.createProgram(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProgramResponseDTO>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponseDTO> getProgramById(@PathVariable String id) {
        return ResponseEntity.ok(programService.getProgramById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<ProgramResponseDTO>> getProgramsByFaculty(@PathVariable String facultyId) {
        return ResponseEntity.ok(programService.getProgramsByFacultyId(facultyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramResponseDTO> updateProgram(@PathVariable String id, @RequestBody ProgramRequestDTO dto) {
        return ResponseEntity.ok(programService.updateProgram(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable String id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}