package com.facultyservice.service;

import com.facultyservice.model.dto.CourseResponseDTO;
import com.facultyservice.model.dto.FacultyEmployeeRequestDTO;
import com.facultyservice.model.dto.FacultyEmployeeResponseDTO;
import com.facultyservice.model.dto.ProgramResponseDTO;
import com.facultyservice.model.dto.StudentResponseDTO;

import java.util.List;

public interface FacultyEmployeeService {
    FacultyEmployeeResponseDTO createEmployee(FacultyEmployeeRequestDTO dto);
    FacultyEmployeeResponseDTO getEmployeeById(String id);
    List<FacultyEmployeeResponseDTO> getAllEmployees();
    List<FacultyEmployeeResponseDTO> getEmployeesByFacultyId(String facultyId);
    FacultyEmployeeResponseDTO updateEmployee(String id, FacultyEmployeeRequestDTO dto);
    void deleteEmployee(String id);
    FacultyEmployeeResponseDTO getEmployeeByEmail(String email);
    void addCourseToEmployee(String employeeId, String courseId);
    void removeCourseFromEmployee(String employeeId, String courseId);
    void addProgramToEmployee(String employeeId, String programId);
    void removeProgramFromEmployee(String employeeId, String programId);
    List<CourseResponseDTO> getEmployeeCourses(String employeeId);
    List<ProgramResponseDTO> getEmployeePrograms(String employeeId);
    List<StudentResponseDTO> getEmployeeStudents(String employeeId);
}