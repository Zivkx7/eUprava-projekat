package com.facultyservice.service.impl;

import com.facultyservice.model.Student;
import com.facultyservice.model.dto.StudentRequestDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import com.facultyservice.model.Enrollment;
import com.facultyservice.repository.StudentRepository;
import com.facultyservice.repository.EnrollmentRepository;
import com.facultyservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setIndexNo(dto.getIndexNo());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setStatus(dto.getStatus());
        return mapToDTO(studentRepository.save(student));
    }

    @Override
    public StudentResponseDTO getStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return mapToDTO(student);
    }

    @Override
    public StudentResponseDTO getStudentByIndexNo(String indexNo) {
        Student student = studentRepository.findByIndexNo(indexNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return mapToDTO(student);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO updateStudent(String id, StudentRequestDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setIndexNo(dto.getIndexNo());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setStatus(dto.getStatus());
        return mapToDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Double calculateGPA(String studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .filter(e -> e.getGrade() != null)
                .mapToInt(Enrollment::getGrade)
                .average()
                .orElse(0.0);
    }

    private StudentResponseDTO mapToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setIndexNo(student.getIndexNo());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setStatus(student.getStatus());
        dto.setCreatedAt(student.getCreatedAt().toString());
        return dto;
    }
}