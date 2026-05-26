package com.facultyservice.service;

import com.facultyservice.model.dto.CourseRequestDTO;
import com.facultyservice.model.dto.CourseResponseDTO;
import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO dto);
    CourseResponseDTO getCourseById(String id);
    List<CourseResponseDTO> getAllCourses();
    List<CourseResponseDTO> getCoursesByProgramId(String programId);
    CourseResponseDTO updateCourse(String id, CourseRequestDTO dto);
    void deleteCourse(String id);
}