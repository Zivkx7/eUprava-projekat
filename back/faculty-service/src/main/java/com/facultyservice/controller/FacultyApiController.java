package com.facultyservice.controller;

import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import com.facultyservice.service.FacultyEmployeeService;
import com.facultyservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class FacultyApiController {

    private final StudentService studentService;
    private final FacultyEmployeeService employeeService;

    // Verifikacija studenta po broju indeksa + email — poziva Služba za zapošljavanje
    @PostMapping("/students/verify")
    public ResponseEntity<Map<String, Object>> verifyStudent(@RequestBody Map<String, String> body) {
        String indexNo = body.get("indexNo");
        String email = body.get("email");
        try {
            StudentResponseDTO student = studentService.getStudentByIndexNo(indexNo);
            boolean match = student != null && student.getEmail() != null
                    && student.getEmail().equalsIgnoreCase(email);
            if (!match) {
                return ResponseEntity.ok(Map.of("verified", false));
            }
            Double gpa = studentService.calculateGPA(student.getId());
            Map<String, Object> resp = new HashMap<>();
            resp.put("verified", true);
            resp.put("studentId", student.getId());
            resp.put("indexNo", student.getIndexNo());
            resp.put("name", student.getName());
            resp.put("email", student.getEmail());
            resp.put("programName", student.getProgramName());
            resp.put("status", student.getStatus());
            resp.put("graduated", "GRADUATED".equals(student.getStatus()));
            resp.put("officialGPA", gpa);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException ex) {
            return ResponseEntity.ok(Map.of("verified", false));
        }
    }

    // Lista zaposlenih — za verifikaciju radnih mesta
    @GetMapping("/employees")
    public ResponseEntity<List<FacultyEmployeeResponseDTO>> listEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
}