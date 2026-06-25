package com.facultyservice.service.impl;

import com.facultyservice.model.Course;
import com.facultyservice.model.dto.CourseRequestDTO;
import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.repository.CourseRepository;
import com.facultyservice.repository.ProgramRepository;
import com.facultyservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ProgramRepository programRepository;

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO dto) {
        Course course = new Course();
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setEcts(dto.getEcts());
        course.setProgram(programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found")));
        return mapToDTO(courseRepository.save(course));
    }

    @Override
    public CourseResponseDTO getCourseById(String id) {
        return mapToDTO(courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found")));
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponseDTO> getCoursesByProgramId(String programId) {
        return courseRepository.findByProgramId(programId)
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDTO updateCourse(String id, CourseRequestDTO dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setEcts(dto.getEcts());
        course.setProgram(programRepository.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Program not found")));
        return mapToDTO(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    private CourseResponseDTO mapToDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setEcts(course.getEcts());
        dto.setProgramId(course.getProgram().getId());
        return dto;
    }
}