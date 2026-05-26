package com.facultyservice.service.impl;

import com.facultyservice.model.Faculty;
import com.facultyservice.model.dto.FacultyRequestDTO;
import com.facultyservice.model.dto.FacultyResponseDTO;
import com.facultyservice.repository.FacultyRepository;
import com.facultyservice.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Override
    public FacultyResponseDTO createFaculty(FacultyRequestDTO dto) {
        Faculty faculty = new Faculty();
        faculty.setName(dto.getName());
        faculty.setAddress(dto.getAddress());
        return mapToDTO(facultyRepository.save(faculty));
    }

    @Override
    public FacultyResponseDTO getFacultyById(String id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        return mapToDTO(faculty);
    }

    @Override
    public List<FacultyResponseDTO> getAllFaculties() {
        return facultyRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FacultyResponseDTO updateFaculty(String id, FacultyRequestDTO dto) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        faculty.setName(dto.getName());
        faculty.setAddress(dto.getAddress());
        return mapToDTO(facultyRepository.save(faculty));
    }

    @Override
    public void deleteFaculty(String id) {
        facultyRepository.deleteById(id);
    }

    private FacultyResponseDTO mapToDTO(Faculty faculty) {
        FacultyResponseDTO dto = new FacultyResponseDTO();
        dto.setId(faculty.getId());
        dto.setName(faculty.getName());
        dto.setAddress(faculty.getAddress());
        return dto;
    }
}