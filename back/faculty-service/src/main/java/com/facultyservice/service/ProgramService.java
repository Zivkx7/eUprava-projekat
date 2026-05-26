package com.facultyservice.service;

import com.facultyservice.model.dto.ProgramRequestDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import java.util.List;

public interface ProgramService {
    ProgramResponseDTO createProgram(ProgramRequestDTO dto);
    ProgramResponseDTO getProgramById(String id);
    List<ProgramResponseDTO> getAllPrograms();
    List<ProgramResponseDTO> getProgramsByFacultyId(String facultyId);
    ProgramResponseDTO updateProgram(String id, ProgramRequestDTO dto);
    void deleteProgram(String id);
}