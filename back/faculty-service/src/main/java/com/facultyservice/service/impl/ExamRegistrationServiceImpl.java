package com.facultyservice.service.impl;

import com.facultyservice.model.Exam;
import com.facultyservice.model.ExamRegistration;
import com.facultyservice.model.Student;
import com.facultyservice.model.dto.ExamRegistrationRequestDTO;
import com.facultyservice.model.dto.ExamRegistrationResponseDTO;
import com.facultyservice.repository.EnrollmentRepository;
import com.facultyservice.repository.ExamRegistrationRepository;
import com.facultyservice.repository.ExamRepository;
import com.facultyservice.repository.StudentRepository;
import com.facultyservice.service.ExamRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamRegistrationServiceImpl implements ExamRegistrationService {

    private final ExamRegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public ExamRegistrationResponseDTO register(ExamRegistrationRequestDTO dto) {
        var existing = registrationRepository.findByStudentIdAndExamId(dto.getStudentId(), dto.getExamId());
        if (existing.isPresent() && !existing.get().getStatus().equals("FAILED")) {
            throw new RuntimeException("Student je već prijavljen na ovaj ispit!");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Exam exam = examRepository.findById(dto.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamRegistration registration = new ExamRegistration();
        registration.setStudent(student);
        registration.setExam(exam);
        registration.setStatus("REGISTERED");
        return mapToDTO(registrationRepository.save(registration));
    }

    @Override
    public List<ExamRegistrationResponseDTO> getByStudent(String studentId) {
        return registrationRepository.findByStudentId(studentId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamRegistrationResponseDTO> getByExam(String examId) {
        return registrationRepository.findByExamId(examId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public ExamRegistrationResponseDTO gradeRegistration(String registrationId, Integer grade) {
        ExamRegistration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        // ocena se ne moze uneti pre nego sto prodje vreme ispita
        if (reg.getExam().getDateTime().isAfter(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Ocena se ne može uneti pre održavanja ispita!");
        }

        if (grade >= 6) {
            reg.setStatus("PASSED");
            String studentId = reg.getStudent().getId();
            String courseId = reg.getExam().getCourse().getId();
            enrollmentRepository.findByStudentId(studentId).stream()
                    .filter(e -> e.getCourse().getId().equals(courseId))
                    .findFirst()
                    .ifPresent(enrollment -> {
                        enrollment.setGrade(grade);
                        enrollmentRepository.save(enrollment);
                    });
        } else {
            reg.setStatus("FAILED");
        }
        return mapToDTO(registrationRepository.save(reg));
    }
    @Override
    public void cancelRegistration(String id) {
        registrationRepository.deleteById(id);
    }

    private ExamRegistrationResponseDTO mapToDTO(ExamRegistration reg) {
        ExamRegistrationResponseDTO dto = new ExamRegistrationResponseDTO();
        dto.setId(reg.getId());
        dto.setStudentId(reg.getStudent().getId());
        dto.setStudentName(reg.getStudent().getName());
        dto.setExamId(reg.getExam().getId());
        dto.setCourseName(reg.getExam().getCourse().getName());
        dto.setExamDateTime(reg.getExam().getDateTime().toString());
        dto.setStatus(reg.getStatus());
        dto.setRegisteredAt(reg.getRegisteredAt().toString());
        return dto;
    }
}