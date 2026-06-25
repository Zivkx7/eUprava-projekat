package com.employmentservice.service;

import com.employmentservice.model.dto.InterviewRequestDTO;
import com.employmentservice.model.dto.InterviewResponseDTO;
import com.employmentservice.model.dto.InviteResponseDTO;
import java.util.List;

public interface InterviewService {
    InterviewResponseDTO scheduleInterview(InterviewRequestDTO dto);
    InterviewResponseDTO getById(String id);
    List<InterviewResponseDTO> getAll();
    List<InterviewResponseDTO> getByApplication(String applicationId);
    InterviewResponseDTO updateInterview(String id, InterviewRequestDTO dto);
    void deleteInterview(String id);
    InviteResponseDTO sendInvite(String id);
}
