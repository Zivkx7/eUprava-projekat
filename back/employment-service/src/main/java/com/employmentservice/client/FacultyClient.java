package com.employmentservice.client;

import com.employmentservice.model.dto.FacultyEmployeeDTO;
import com.employmentservice.model.dto.StudentVerificationDTO;
import com.employmentservice.model.dto.StudentVerifyRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Klijent ka mikroservisu Fakultet (REST endpointi pod prefiksom /internal).
 * Verifikacija studenta ide preko broja indeksa i studentskog mejla (POST, telo zahteva),
 * uz zajednički tajni ključ u headeru X-Internal-Key (samo Služba sme da zove /internal).
 */
@Component
@RequiredArgsConstructor
public class FacultyClient {

    private final RestTemplate restTemplate;

    @Value("${faculty.service.url}")
    private String facultyUrl;

    @Value("${faculty.internal.key}")
    private String internalKey;

    /**
     * POST /internal/students/verify  { indexNo, email }
     * Vraća zvanične podatke studenta ako se indeks i mejl poklope, inače null / verified=false.
     */
    public StudentVerificationDTO verifyStudent(String indexNo, String email) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Internal-Key", internalKey);
            HttpEntity<StudentVerifyRequestDTO> request =
                    new HttpEntity<>(new StudentVerifyRequestDTO(indexNo, email), headers);
            ResponseEntity<StudentVerificationDTO> response = restTemplate.postForEntity(
                    facultyUrl + "/internal/students/verify", request, StudentVerificationDTO.class);
            return response.getBody();
        } catch (RestClientException e) {
            // Student nije pronađen, mejl se ne poklapa, ili je Fakultet nedostupan
            return null;
        }
    }

    /** GET /internal/employees — lista zaposlenih (verifikacija radnih mesta). */
    public List<FacultyEmployeeDTO> listEmployees() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Key", internalKey);
            ResponseEntity<FacultyEmployeeDTO[]> response = restTemplate.exchange(
                    facultyUrl + "/internal/employees", HttpMethod.GET,
                    new HttpEntity<>(headers), FacultyEmployeeDTO[].class);
            FacultyEmployeeDTO[] employees = response.getBody();
            return employees != null ? Arrays.asList(employees) : Collections.emptyList();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }
}
