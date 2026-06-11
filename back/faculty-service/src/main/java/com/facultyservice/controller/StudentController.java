package com.facultyservice.controller;

import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.StudentRequestDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import com.facultyservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.createStudent(dto));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/index/{indexNo}")
    public ResponseEntity<StudentResponseDTO> getStudentByIndexNo(@PathVariable String indexNo) {
        return ResponseEntity.ok(studentService.getStudentByIndexNo(indexNo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable String id, @RequestBody StudentRequestDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/gpa")
    public ResponseEntity<Double> getGPA(@PathVariable String id) {
        return ResponseEntity.ok(studentService.calculateGPA(id));
    }
    @GetMapping("/by-email")
    public ResponseEntity<StudentResponseDTO> getStudentByEmail(@RequestParam String email) {
        return ResponseEntity.ok(studentService.getStudentByEmail(email));
    }
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getStudentCourses(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentCourses(id));
    }
}