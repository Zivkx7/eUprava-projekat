package com.facultyservice.controller;

import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.service.FacultyEmployeeService;
import com.facultyservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class FacultyApiController {

    private final StudentService studentService;
    private final FacultyEmployeeService employeeService;

    // Zvanični GPA studenta — poziva Služba za zapošljavanje
    @GetMapping("/gpa/{studentId}")
    public ResponseEntity<Map<String, Object>> getOfficialGPA(@PathVariable String studentId) {
        Double gpa = studentService.calculateGPA(studentId);
        var student = studentService.getStudentById(studentId);
        return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "indexNo", student.getIndexNo(),
                "name", student.getName(),
                "officialGPA", gpa
        ));
    }

    // Lista zaposlenih — za verifikaciju radnih mesta
    @GetMapping("/employees")
    public ResponseEntity<List<FacultyEmployeeResponseDTO>> listEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}