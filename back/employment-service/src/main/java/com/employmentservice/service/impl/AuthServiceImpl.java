package com.employmentservice.service.impl;

import com.employmentservice.auth.JwtService;
import com.employmentservice.model.Role;
import com.employmentservice.model.User;
import com.employmentservice.model.dto.AuthRequestDTO;
import com.employmentservice.model.dto.AuthResponseDTO;
import com.employmentservice.model.dto.RegisterRequestDTO;
import com.employmentservice.repository.UserRepository;
import com.employmentservice.service.AuthService;
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
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.valueOf(dto.getRole()));
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token, user.getRole().name());
    }
}
