package com.facultyservice.service.impl;

import com.facultyservice.model.Course;
import com.facultyservice.model.Enrollment;
import com.facultyservice.model.Role;
import com.facultyservice.model.Student;
import com.facultyservice.model.User;
import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.StudentRequestDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import com.facultyservice.repository.EnrollmentRepository;
import com.facultyservice.repository.ProgramRepository;
import com.facultyservice.repository.StudentRepository;
import com.facultyservice.repository.UserRepository;
import com.facultyservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProgramRepository programRepository;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setIndexNo(dto.getIndexNo());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setStatus(dto.getStatus());
        student.setProgram(programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found")));
        studentRepository.save(student);

        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        return mapToDTO(student);
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
        if (dto.getProgramId() != null) {
            student.setProgram(programRepository.findById(dto.getProgramId())
                    .orElseThrow(() -> new RuntimeException("Program not found")));
        }
        return mapToDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(String id) {
        enrollmentRepository.deleteAll(enrollmentRepository.findByStudentId(id));
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

    @Override
    public StudentResponseDTO getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return mapToDTO(student);
    }

    @Override
    public List<CourseResponseDTO> getStudentCourses(String studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(e -> {
                    Course course = e.getCourse();
                    CourseResponseDTO dto = new CourseResponseDTO();
                    dto.setId(course.getId());
                    dto.setCode(course.getCode());
                    dto.setName(course.getName());
                    dto.setEcts(course.getEcts());
                    dto.setProgramId(course.getProgram().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private StudentResponseDTO mapToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setIndexNo(student.getIndexNo());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setStatus(student.getStatus());
        dto.setProgramId(student.getProgram().getId());
        dto.setProgramName(student.getProgram().getName());
        dto.setCreatedAt(student.getCreatedAt().toString());
        return dto;
    }
}