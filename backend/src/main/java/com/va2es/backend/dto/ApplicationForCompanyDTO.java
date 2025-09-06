package com.va2es.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ApplicationForCompanyDTO {
    public Long applicationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime applicationDate;

    public Long vacancyId;
    public String vacancyTitle;
    public Long studentId;
    public String studentName;
    public String studentCourse;

    public ApplicationForCompanyDTO(Long applicationId, LocalDateTime applicationDate, Long vacancyId, String vacancyTitle, Long studentId, String studentName, String studentCourse) {
        this.applicationId = applicationId;
        this.applicationDate = applicationDate;
        this.vacancyId = vacancyId;
        this.vacancyTitle = vacancyTitle;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCourse = studentCourse;
    }
}