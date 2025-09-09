package com.va2es.backend.dto;

import java.time.LocalDate;

public class StudentResponseDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private String cpf;
    private String phone;
    private String course;
    private Integer currentPeriod;
    private String academicSummary;
    private String userEmail;

    // Construtor
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

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(Integer currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getAcademicSummary() {
        return academicSummary;
    }

    public void setAcademicSummary(String academicSummary) {
        this.academicSummary = academicSummary;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}