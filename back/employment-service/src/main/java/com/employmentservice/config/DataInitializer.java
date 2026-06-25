package com.employmentservice.config;

import com.employmentservice.model.Role;
import com.employmentservice.model.User;
import com.employmentservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Kreira podrazumevani ADMIN nalog pri prvom pokretanju,
 * da bi prijava na sistem radila bez dodatne registracije.
 * Login: admin@zaposljavanje.rs / admin
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminUsername = "admin@zaposljavanje.rs";
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Kreiran podrazumevani ADMIN nalog: " + adminUsername + " / admin");
        }
    }
}
