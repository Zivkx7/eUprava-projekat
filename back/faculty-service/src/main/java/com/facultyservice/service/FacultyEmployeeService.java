package com.facultyservice.service;

import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import java.util.List;

public interface FacultyEmployeeService {
    FacultyEmployeeResponseDTO createEmployee(FacultyEmployeeRequestDTO dto);
    FacultyEmployeeResponseDTO getEmployeeById(String id);
    List<FacultyEmployeeResponseDTO> getAllEmployees();
    List<FacultyEmployeeResponseDTO> getEmployeesByFacultyId(String facultyId);
    FacultyEmployeeResponseDTO updateEmployee(String id, FacultyEmployeeRequestDTO dto);
    void deleteEmployee(String id);
    FacultyEmployeeResponseDTO getEmployeeByEmail(String email);
}