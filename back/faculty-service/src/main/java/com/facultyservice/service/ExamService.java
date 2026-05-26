package com.facultyservice.service;

import com.facultyservice.model.dto.ExamRequestDTO;
import com.facultyservice.model.dto.ExamResponseDTO;
import java.util.List;

public interface ExamService {
    ExamResponseDTO createExam(ExamRequestDTO dto);
    ExamResponseDTO getExamById(String id);
    List<ExamResponseDTO> getAllExams();
    List<ExamResponseDTO> getExamsByCourseId(String courseId);
    ExamResponseDTO updateExam(String id, ExamRequestDTO dto);
    void deleteExam(String id);
}