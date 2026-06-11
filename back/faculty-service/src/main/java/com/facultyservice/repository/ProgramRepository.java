package com.facultyservice.repository;

import com.facultyservice.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProgramRepository extends JpaRepository<Program, String> {
}