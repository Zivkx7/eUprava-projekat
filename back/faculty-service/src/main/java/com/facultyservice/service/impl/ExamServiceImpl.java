package com.facultyservice.service.impl;

import com.facultyservice.model.Exam;
import com.facultyservice.model.dto.ExamRequestDTO;
import com.facultyservice.model.dto.ExamResponseDTO;
import com.facultyservice.repository.CourseRepository;
import com.facultyservice.repository.ExamRegistrationRepository;
import com.facultyservice.repository.ExamRepository;
import com.facultyservice.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final ExamRegistrationRepository examRegistrationRepository;

    @Override
    public ExamResponseDTO createExam(ExamRequestDTO dto) {
        Exam exam = new Exam();
        exam.setRoom(dto.getRoom());
        exam.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        exam.setCourse(courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")));
        return mapToDTO(examRepository.save(exam));
    }

    @Override
    public ExamResponseDTO getExamById(String id) {
        return mapToDTO(examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found")));
    }

    @Override
    public List<ExamResponseDTO> getAllExams() {
        return examRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponseDTO> getExamsByCourseId(String courseId) {
        return examRepository.findByCourseId(courseId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponseDTO updateExam(String id, ExamRequestDTO dto) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        exam.setRoom(dto.getRoom());
        exam.setDateTime(LocalDateTime.parse(dto.getDateTime()));
        exam.setCourse(courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")));
        return mapToDTO(examRepository.save(exam));
    }

    @Override
    public void deleteExam(String id) {
        examRegistrationRepository.deleteAll(examRegistrationRepository.findByExamId(id));
        examRepository.deleteById(id);
    }

    private ExamResponseDTO mapToDTO(Exam exam) {
        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setId(exam.getId());
        dto.setCourseId(exam.getCourse().getId());
        dto.setDateTime(exam.getDateTime().toString());
        dto.setRoom(exam.getRoom());
        return dto;
    }
}