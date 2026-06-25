package com.facultyservice.repository;

import com.facultyservice.model.ExamRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, String> {
    List<ExamRegistration> findByStudentId(String studentId);
    List<ExamRegistration> findByExamId(String examId);
    Optional<ExamRegistration> findByStudentIdAndExamId(String studentId, String examId);
}