package com.facultyservice.service;

import com.facultyservice.model.dto.AuthRequestDTO;
import com.facultyservice.model.dto.AuthResponseDTO;
import com.facultyservice.model.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO dto);
    AuthResponseDTO register(RegisterRequestDTO dto);
}