package com.facultyservice.service;

import com.facultyservice.model.dto.EnrollmentRequestDTO;
import com.facultyservice.model.dto.EnrollmentResponseDTO;
import java.util.List;

public interface EnrollmentService {
    EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO dto);
    EnrollmentResponseDTO getEnrollmentById(String id);
    List<EnrollmentResponseDTO> getEnrollmentsByStudentId(String studentId);
    List<EnrollmentResponseDTO> getEnrollmentsByCourseId(String courseId);
    EnrollmentResponseDTO updateGrade(String enrollmentId, Integer grade);
    void deleteEnrollment(String id);
}