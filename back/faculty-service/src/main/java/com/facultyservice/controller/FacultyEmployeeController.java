package com.facultyservice.controller;

import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.service.FacultyEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class FacultyEmployeeController {

    private final FacultyEmployeeService employeeService;

    @PostMapping
    public ResponseEntity<FacultyEmployeeResponseDTO> createEmployee(@RequestBody FacultyEmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @GetMapping
    public ResponseEntity<List<FacultyEmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyEmployeeResponseDTO> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<FacultyEmployeeResponseDTO>> getByFaculty(@PathVariable String facultyId) {
        return ResponseEntity.ok(employeeService.getEmployeesByFacultyId(facultyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyEmployeeResponseDTO> updateEmployee(@PathVariable String id, @RequestBody FacultyEmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}