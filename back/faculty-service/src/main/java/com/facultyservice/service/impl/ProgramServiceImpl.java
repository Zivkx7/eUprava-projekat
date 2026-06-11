package com.facultyservice.service.impl;

import com.facultyservice.model.Program;
import com.facultyservice.model.dto.ProgramRequestDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import com.facultyservice.repository.ProgramRepository;
import com.facultyservice.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    @Override
    public ProgramResponseDTO createProgram(ProgramRequestDTO dto) {
        Program program = new Program();
        program.setName(dto.getName());
        program.setDegree(dto.getDegree());
        return mapToDTO(programRepository.save(program));
    }

    @Override
    public ProgramResponseDTO getProgramById(String id) {
        return mapToDTO(programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found")));
    }

    @Override
    public List<ProgramResponseDTO> getAllPrograms() {
        return programRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProgramResponseDTO> getProgramsByFacultyId(String facultyId) {
        return programRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProgramResponseDTO updateProgram(String id, ProgramRequestDTO dto) {
        Program program = programRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Program not found"));
        program.setName(dto.getName());
        program.setDegree(dto.getDegree());
        return mapToDTO(programRepository.save(program));
    }

    @Override
    public void deleteProgram(String id) {
        programRepository.deleteById(id);
    }

    private ProgramResponseDTO mapToDTO(Program program) {
        ProgramResponseDTO dto = new ProgramResponseDTO();
        dto.setId(program.getId());
        dto.setName(program.getName());
        dto.setDegree(program.getDegree());
        return dto;
    }
}