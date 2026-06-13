package com.facultyservice.service;

import com.facultyservice.model.dto.ExamRegistrationRequestDTO;
import com.facultyservice.model.dto.ExamRegistrationResponseDTO;
import java.util.List;

public interface ExamRegistrationService {
    ExamRegistrationResponseDTO register(ExamRegistrationRequestDTO dto);
    List<ExamRegistrationResponseDTO> getByStudent(String studentId);
    List<ExamRegistrationResponseDTO> getByExam(String examId);
    void cancelRegistration(String id);
    ExamRegistrationResponseDTO gradeRegistration(String registrationId, Integer grade);
}