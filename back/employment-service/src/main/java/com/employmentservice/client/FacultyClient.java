package com.employmentservice.client;

import com.employmentservice.model.dto.FacultyEmployeeDTO;
import com.employmentservice.model.dto.FacultyGpaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Klijent ka mikroservisu Fakultet (REST endpointi pod prefiksom /internal).
 * Koristi se za zavisne funkcije: verifikacija obrazovanja, zvanični GPA, lista zaposlenih.
 */
@Component
@RequiredArgsConstructor
public class FacultyClient {

    private final RestTemplate restTemplate;

    @Value("${faculty.service.url}")
    private String facultyUrl;

    // GET /internal/gpa/{studentId} — zvanični GPA studenta
    public FacultyGpaDTO getOfficialGPA(String studentId) {
        try {
            return restTemplate.getForObject(
                    facultyUrl + "/internal/gpa/" + studentId, FacultyGpaDTO.class);
        } catch (RestClientException e) {
            // Student ne postoji na fakultetu ili je fakultet nedostupan
            return null;
        }
    }

    // GET /internal/employees — lista zaposlenih (verifikacija radnih mesta)
    public List<FacultyEmployeeDTO> listEmployees() {
        try {
            FacultyEmployeeDTO[] employees = restTemplate.getForObject(
                    facultyUrl + "/internal/employees", FacultyEmployeeDTO[].class);
            return employees != null ? Arrays.asList(employees) : Collections.emptyList();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }
}
