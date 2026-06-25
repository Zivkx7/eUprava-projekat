package com.facultyservice.controller;

import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
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
    @PostMapping("/{employeeId}/courses/{courseId}")
    public ResponseEntity<Void> addCourse(@PathVariable String employeeId, @PathVariable String courseId) {
        employeeService.addCourseToEmployee(employeeId, courseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{employeeId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourse(@PathVariable String employeeId, @PathVariable String courseId) {
        employeeService.removeCourseFromEmployee(employeeId, courseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{employeeId}/programs/{programId}")
    public ResponseEntity<Void> addProgram(@PathVariable String employeeId, @PathVariable String programId) {
        employeeService.addProgramToEmployee(employeeId, programId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{employeeId}/programs/{programId}")
    public ResponseEntity<Void> removeProgram(@PathVariable String employeeId, @PathVariable String programId) {
        employeeService.removeProgramFromEmployee(employeeId, programId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{employeeId}/courses")
    public ResponseEntity<List<CourseResponseDTO>> getEmployeeCourses(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeCourses(employeeId));
    }

    @GetMapping("/{employeeId}/programs")
    public ResponseEntity<List<ProgramResponseDTO>> getEmployeePrograms(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeePrograms(employeeId));
    }
    @GetMapping("/by-email")
    public ResponseEntity<FacultyEmployeeResponseDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }
    @GetMapping("/{employeeId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getEmployeeStudents(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeStudents(employeeId));
    }
}