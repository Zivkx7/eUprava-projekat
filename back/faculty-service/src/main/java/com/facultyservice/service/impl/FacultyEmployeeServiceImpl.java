package com.facultyservice.service.impl;

import com.facultyservice.model.FacultyEmployee;
import com.facultyservice.model.Role;
import com.facultyservice.model.User;
import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.repository.FacultyEmployeeRepository;
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
}