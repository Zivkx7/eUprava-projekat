package com.employmentservice.service;

import com.employmentservice.model.dto.AuthRequestDTO;
import com.employmentservice.model.dto.AuthResponseDTO;
import com.employmentservice.model.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO dto);
    AuthResponseDTO register(RegisterRequestDTO dto);
}
