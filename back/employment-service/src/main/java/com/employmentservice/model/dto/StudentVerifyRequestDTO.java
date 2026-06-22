package com.employmentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Telo zahteva ka Fakultetu za verifikaciju studenta (indeks + studentski mejl).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVerifyRequestDTO {
    private String indexNo;
    private String email;
}
