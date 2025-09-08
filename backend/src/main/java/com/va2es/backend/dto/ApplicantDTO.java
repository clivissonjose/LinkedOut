package com.va2es.backend.dto;

import java.time.LocalDateTime;

public class ApplicantDTO {
    public Long studentId;
    public String studentName;
    public String studentCourse;
    public LocalDateTime applicationDate;

    public ApplicantDTO(Long studentId, String studentName, String studentCourse, LocalDateTime applicationDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCourse = studentCourse;
        this.applicationDate = applicationDate;
    }
}