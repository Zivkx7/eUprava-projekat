package com.facultyservice.service.impl;

import com.facultyservice.model.Enrollment;
import com.facultyservice.model.dto.EnrollmentRequestDTO;
import com.facultyservice.model.dto.EnrollmentResponseDTO;
import com.facultyservice.repository.CourseRepository;
import com.facultyservice.repository.EnrollmentRepository;
import com.facultyservice.repository.StudentRepository;
import com.facultyservice.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponseDTO enrollStudent(EnrollmentRequestDTO dto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found")));
        enrollment.setCourse(courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")));
        return mapToDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponseDTO getEnrollmentById(String id) {
        return mapToDTO(enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found")));
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponseDTO> getEnrollmentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentResponseDTO updateGrade(String enrollmentId, Integer grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setGrade(grade);
        return mapToDTO(enrollmentRepository.save(enrollment));
    }

    @Override
    public void deleteEnrollment(String id) {
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentResponseDTO mapToDTO(Enrollment enrollment) {
        EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setGrade(enrollment.getGrade());
        dto.setEnrolledAt(enrollment.getEnrolledAt().toString());
        return dto;
    }
}