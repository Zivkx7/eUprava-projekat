package com.employmentservice.service.impl;

import com.employmentservice.model.Employer;
import com.employmentservice.model.Role;
import com.employmentservice.model.User;
import com.employmentservice.model.dto.EmployerRequestDTO;
import com.employmentservice.model.dto.EmployerResponseDTO;
import com.employmentservice.repository.EmployerRepository;
import com.employmentservice.repository.UserRepository;
import com.employmentservice.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployerResponseDTO createEmployer(EmployerRequestDTO dto) {
        Employer employer = new Employer();
        employer.setName(dto.getName());
        employer.setSector(dto.getSector());
        employer.setEmail(dto.getEmail());
        employerRepository.save(employer);

        // Automatsko kreiranje EMPLOYER korisničkog naloga
        User user = new User();
        user.setUsername(dto.getEmail());
        user.setPassword(passwordEncoder.encode(
                dto.getPassword() != null ? dto.getPassword() : "employer"));
        user.setRole(Role.EMPLOYER);
        userRepository.save(user);

        return mapToDTO(employer);
    }

    @Override
    public EmployerResponseDTO getEmployerById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public EmployerResponseDTO getEmployerByEmail(String email) {
        Employer employer = employerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        return mapToDTO(employer);
    }

    @Override
    public List<EmployerResponseDTO> getAllEmployers() {
        return employerRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployerResponseDTO updateEmployer(String id, EmployerRequestDTO dto) {
        Employer employer = findEntity(id);
        employer.setName(dto.getName());
        employer.setSector(dto.getSector());
        employer.setEmail(dto.getEmail());
        return mapToDTO(employerRepository.save(employer));
    }

    @Override
    public void deleteEmployer(String id) {
        employerRepository.deleteById(id);
    }

    private Employer findEntity(String id) {
        return employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
    }

    private EmployerResponseDTO mapToDTO(Employer employer) {
        EmployerResponseDTO dto = new EmployerResponseDTO();
        dto.setId(employer.getId());
        dto.setName(employer.getName());
        dto.setSector(employer.getSector());
        dto.setEmail(employer.getEmail());
        if (employer.getCreatedAt() != null) {
            dto.setCreatedAt(employer.getCreatedAt().toString());
        }
        return dto;
    }
}
