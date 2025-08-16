package com.va2es.backend.dto;

import java.time.LocalDate;

public class StudentResponseDTO {
    public Long id;
    public String fullName;
    public LocalDate birthDate;
    public String cpf;
    public String phone;
    public String course;
    public Integer currentPeriod;
    public String academicSummary;
    public String userEmail;

    public StudentResponseDTO(Long id, String fullName, LocalDate birthDate, String cpf,
                              String phone, String course, int currentPeriod,
                              String academicSummary, String userEmail) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.phone = phone;
        this.course = course;
        this.currentPeriod = currentPeriod;
        this.academicSummary = academicSummary;
        this.userEmail = userEmail;
    }
}
