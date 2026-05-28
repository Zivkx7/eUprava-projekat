package com.facultyservice.service.impl;

import com.facultyservice.auth.JwtService;
import com.facultyservice.model.Role;
import com.facultyservice.model.User;
import com.facultyservice.model.dto.AuthRequestDTO;
import com.facultyservice.model.dto.AuthResponseDTO;
import com.facultyservice.model.dto.RegisterRequestDTO;
import com.facultyservice.repository.UserRepository;
import com.facultyservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.valueOf(dto.getRole()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }
}