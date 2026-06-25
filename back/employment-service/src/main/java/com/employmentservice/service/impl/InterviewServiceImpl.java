package com.employmentservice.service.impl;

import com.employmentservice.model.Application;
import com.employmentservice.model.Interview;
import com.employmentservice.model.dto.InterviewRequestDTO;
import com.employmentservice.model.dto.InterviewResponseDTO;
import com.employmentservice.model.dto.InviteResponseDTO;
import com.employmentservice.repository.ApplicationRepository;
import com.employmentservice.repository.InterviewRepository;
import com.employmentservice.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public InterviewResponseDTO scheduleInterview(InterviewRequestDTO dto) {
        Application application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setDateTime(parseDateTime(dto.getDateTime()));
        interview.setLocation(dto.getLocation());
        interview.setType(dto.getType());
        interview.setInviteSent(false);
        Interview saved = interviewRepository.save(interview);

        // Zakazivanje intervjua pomera prijavu u status INTERVIEW
        application.setStatus("INTERVIEW");
        applicationRepository.save(application);

        return mapToDTO(saved);
    }

    @Override
    public InterviewResponseDTO getById(String id) {
        return mapToDTO(findEntity(id));
    }

    @Override
    public List<InterviewResponseDTO> getAll() {
        return interviewRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDTO> getByApplication(String applicationId) {
        return interviewRepository.findByApplicationId(applicationId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InterviewResponseDTO updateInterview(String id, InterviewRequestDTO dto) {
        Interview interview = findEntity(id);
        interview.setDateTime(parseDateTime(dto.getDateTime()));
        interview.setLocation(dto.getLocation());
        interview.setType(dto.getType());
        return mapToDTO(interviewRepository.save(interview));
    }

    @Override
    public void deleteInterview(String id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public InviteResponseDTO sendInvite(String id) {
        Interview interview = findEntity(id);
        interview.setInviteSent(true);
        interviewRepository.save(interview);

        String candidateName = interview.getApplication() != null && interview.getApplication().getCandidate() != null
                ? interview.getApplication().getCandidate().getFullName() : "kandidat";
        String message = "Poštovani/a " + candidateName + ", pozvani ste na intervju "
                + (interview.getDateTime() != null ? interview.getDateTime() : "")
                + (interview.getLocation() != null ? " — lokacija: " + interview.getLocation() : "")
                + (interview.getType() != null ? " (" + interview.getType() + ")" : "") + ".";

        // Simulacija slanja pozivnice (u realnom sistemu: email/notifikacija)
        System.out.println(">>> Poslata pozivnica: " + message);
        return new InviteResponseDTO(interview.getId(), message, LocalDateTime.now().toString());
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    private Interview findEntity(String id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    private InterviewResponseDTO mapToDTO(Interview interview) {
        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(interview.getId());
        if (interview.getApplication() != null) {
            dto.setApplicationId(interview.getApplication().getId());
            if (interview.getApplication().getCandidate() != null) {
                dto.setCandidateName(interview.getApplication().getCandidate().getFullName());
            }
            if (interview.getApplication().getJobOffer() != null) {
                dto.setJobOfferTitle(interview.getApplication().getJobOffer().getTitle());
            }
        }
        dto.setDateTime(interview.getDateTime() != null ? interview.getDateTime().toString() : null);
        dto.setLocation(interview.getLocation());
        dto.setType(interview.getType());
        dto.setInviteSent(interview.isInviteSent());
        return dto;
    }
}
