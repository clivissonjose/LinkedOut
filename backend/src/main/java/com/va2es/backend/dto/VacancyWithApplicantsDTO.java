package com.va2es.backend.dto;

import java.util.List;

public class VacancyWithApplicantsDTO {
    public Long vacancyId;
    public String vacancyTitle;
    public List<ApplicantDTO> applicants;

    public VacancyWithApplicantsDTO(Long vacancyId, String vacancyTitle, List<ApplicantDTO> applicants) {
        this.vacancyId = vacancyId;
        this.vacancyTitle = vacancyTitle;
        this.applicants = applicants;
    }
}