package com.employmentservice.repository;

import com.employmentservice.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, String> {
    Optional<Employer> findByEmail(String email);
}
