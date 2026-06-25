package com.facultyservice.repository;

import com.facultyservice.model.FacultyEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FacultyEmployeeRepository extends JpaRepository<FacultyEmployee, String> {
    Optional<FacultyEmployee> findByEmail(String email);
}