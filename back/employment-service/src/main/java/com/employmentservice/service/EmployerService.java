package com.employmentservice.service;

import com.employmentservice.model.dto.EmployerRequestDTO;
import com.employmentservice.model.dto.EmployerResponseDTO;
import java.util.List;

public interface EmployerService {
    EmployerResponseDTO createEmployer(EmployerRequestDTO dto);
    EmployerResponseDTO getEmployerById(String id);
    EmployerResponseDTO getEmployerByEmail(String email);
    List<EmployerResponseDTO> getAllEmployers();
    EmployerResponseDTO updateEmployer(String id, EmployerRequestDTO dto);
    void deleteEmployer(String id);
}
