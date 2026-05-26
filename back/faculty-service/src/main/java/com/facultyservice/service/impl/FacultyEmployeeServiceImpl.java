package com.facultyservice.service.impl;

import com.facultyservice.model.FacultyEmployee;
import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.repository.FacultyEmployeeRepository;
import com.facultyservice.repository.FacultyRepository;
import com.facultyservice.service.FacultyEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyEmployeeServiceImpl implements FacultyEmployeeService {

    private final FacultyEmployeeRepository employeeRepository;
    private final FacultyRepository facultyRepository;

    @Override
    public FacultyEmployeeResponseDTO createEmployee(FacultyEmployeeRequestDTO dto) {
        FacultyEmployee employee = new FacultyEmployee();
        employee.setFullName(dto.getFullName());
        employee.setRole(dto.getRole());
        employee.setEmail(dto.getEmail());
        employee.setFaculty(facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found")));
        return mapToDTO(employeeRepository.save(employee));
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
        return employeeRepository.findByFacultyId(facultyId)
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
        employee.setFaculty(facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found")));
        return mapToDTO(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    private FacultyEmployeeResponseDTO mapToDTO(FacultyEmployee employee) {
        FacultyEmployeeResponseDTO dto = new FacultyEmployeeResponseDTO();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setRole(employee.getRole());
        dto.setEmail(employee.getEmail());
        dto.setFacultyId(employee.getFaculty().getId());
        return dto;
    }
}