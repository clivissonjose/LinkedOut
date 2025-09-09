package com.va2es.backend.dto;

import java.time.LocalDateTime;

public class ApplicationForStudentDTO {
    public Long applicationId;
    public Long vacancyId;
    public String vacancyTitle;
    public String companyName;
    public LocalDateTime applicationDate;

    public ApplicationForStudentDTO(Long applicationId, Long vacancyId, String vacancyTitle, String companyName, LocalDateTime applicationDate) {
        this.applicationId = applicationId;
        this.vacancyId = vacancyId;
        this.vacancyTitle = vacancyTitle;
        this.companyName = companyName;
        this.applicationDate = applicationDate;
    }
}