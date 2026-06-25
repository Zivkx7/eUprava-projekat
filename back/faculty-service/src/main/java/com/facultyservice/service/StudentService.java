package com.facultyservice.service;

import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.StudentRequestDTO;
import com.facultyservice.model.dto.StudentResponseDTO;
import java.util.List;

public interface StudentService {
    StudentResponseDTO createStudent(StudentRequestDTO dto);
    StudentResponseDTO getStudentById(String id);
    StudentResponseDTO getStudentByIndexNo(String indexNo);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO updateStudent(String id, StudentRequestDTO dto);
    void deleteStudent(String id);
    Double calculateGPA(String studentId);
    StudentResponseDTO getStudentByEmail(String email);
    List<CourseResponseDTO> getStudentCourses(String studentId);
}