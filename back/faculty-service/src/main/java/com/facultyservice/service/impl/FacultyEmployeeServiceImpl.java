package com.facultyservice.service.impl;

import com.facultyservice.model.Course;
import com.facultyservice.model.Enrollment;
import com.facultyservice.model.FacultyEmployee;
import com.facultyservice.model.Program;
import com.facultyservice.model.Role;
import com.facultyservice.model.User;
import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import com.facultyservice.repository.CourseRepository;
import com.facultyservice.repository.EnrollmentRepository;
import com.facultyservice.repository.FacultyEmployeeRepository;
import com.facultyservice.repository.ProgramRepository;
import com.facultyservice.repository.UserRepository;
import com.facultyservice.service.FacultyEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyEmployeeServiceImpl implements FacultyEmployeeService {

    private final FacultyEmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;
    private final EnrollmentRepository enrollmentRepository;
    

    @Override
    public FacultyEmployeeResponseDTO createEmployee(FacultyEmployeeRequestDTO dto) {
        FacultyEmployee employee = new FacultyEmployee();
        employee.setFullName(dto.getFullName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        employeeRepository.save(employee);

        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.PROFESSOR);
        userRepository.save(user);

        return mapToDTO(employee);
    }

    @Override
    public FacultyEmployeeResponseDTO getEmployeeById(String id) {
        return mapToDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found")));
    }

    @Override
    public List<FacultyEmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacultyEmployeeResponseDTO> getEmployeesByFacultyId(String facultyId) {
        return employeeRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyEmployeeResponseDTO updateEmployee(String id, FacultyEmployeeRequestDTO dto) {
        FacultyEmployee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setFullName(dto.getFullName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        return mapToDTO(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public FacultyEmployeeResponseDTO getEmployeeByEmail(String email) {
        FacultyEmployee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return mapToDTO(employee);
    }

    private FacultyEmployeeResponseDTO mapToDTO(FacultyEmployee employee) {
        FacultyEmployeeResponseDTO dto = new FacultyEmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setRole(employee.getRole());
        dto.setEmail(employee.getEmail());
        return dto;
    }
    @Override
    public void addCourseToEmployee(String employeeId, String courseId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!employee.getCourses().contains(course)) {
            employee.getCourses().add(course);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void removeCourseFromEmployee(String employeeId, String courseId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.getCourses().removeIf(c -> c.getId().equals(courseId));
        employeeRepository.save(employee);
    }

    @Override
    public void addProgramToEmployee(String employeeId, String programId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        if (!employee.getPrograms().contains(program)) {
            employee.getPrograms().add(program);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void removeProgramFromEmployee(String employeeId, String programId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.getPrograms().removeIf(p -> p.getId().equals(programId));
        employeeRepository.save(employee);
    }

    @Override
    public List<CourseResponseDTO> getEmployeeCourses(String employeeId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getCourses().stream().map(course -> {
            CourseResponseDTO dto = new CourseResponseDTO();
            dto.setId(course.getId());
            dto.setCode(course.getCode());
            dto.setName(course.getName());
            dto.setEcts(course.getEcts());
            dto.setProgramId(course.getProgram().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProgramResponseDTO> getEmployeePrograms(String employeeId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getPrograms().stream().map(program -> {
            ProgramResponseDTO dto = new ProgramResponseDTO();
            dto.setId(program.getId());
            dto.setName(program.getName());
            dto.setDegree(program.getDegree());
            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public List<StudentResponseDTO> getEmployeeStudents(String employeeId) {
        FacultyEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getCourses().stream()
                .flatMap(course -> enrollmentRepository.findByCourseId(course.getId()).stream())
                .map(Enrollment::getStudent)
                .distinct()
                .map(student -> {
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
                })
                .collect(Collectors.toList());
    }
}