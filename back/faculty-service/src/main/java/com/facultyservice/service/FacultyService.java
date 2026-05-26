package com.facultyservice.service;

import com.facultyservice.model.dto.FacultyRequestDTO;
import com.facultyservice.model.dto.FacultyResponseDTO;
import java.util.List;

public interface FacultyService {
    FacultyResponseDTO createFaculty(FacultyRequestDTO dto);
    FacultyResponseDTO getFacultyById(String id);
    List<FacultyResponseDTO> getAllFaculties();
    FacultyResponseDTO updateFaculty(String id, FacultyRequestDTO dto);
    void deleteFaculty(String id);
}