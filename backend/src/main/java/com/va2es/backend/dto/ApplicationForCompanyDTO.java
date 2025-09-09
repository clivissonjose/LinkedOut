package com.va2es.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ApplicationForCompanyDTO {
    private Long applicationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applicationDate;

    private Long vacancyId;
    private String vacancyTitle;
    private Long studentId;
    private String studentName;
    private String studentCourse;

    public ApplicationForCompanyDTO(Long applicationId, LocalDateTime applicationDate, Long vacancyId, String vacancyTitle, Long studentId, String studentName, String studentCourse) {
        this.applicationId = applicationId;
        this.applicationDate = applicationDate;
        this.vacancyId = vacancyId;
        this.vacancyTitle = vacancyTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCourse = studentCourse;
    }

    // --- Getters e Setters ---

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Long getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Long vacancyId) {
        this.vacancyId = vacancyId;
    }

    public String getVacancyTitle() {
        return vacancyTitle;
    }

    public void setVacancyTitle(String vacancyTitle) {
        this.vacancyTitle = vacancyTitle;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(String studentCourse) {
        this.studentCourse = studentCourse;
    }
}