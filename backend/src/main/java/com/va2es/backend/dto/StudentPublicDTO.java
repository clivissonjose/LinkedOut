package com.va2es.backend.dto;

public class StudentPublicDTO {
   public String fullName;
    public int currentPeriod;
    public String phone;
    public String course;
    public String academicSummary;

    public StudentPublicDTO(String fullName, int currentPeriod, String phone, String course, String academicSummary) {
        this.fullName = fullName;
        this.currentPeriod = currentPeriod;
        this.phone = phone;
        this.course = course;
        this.academicSummary = academicSummary;
    }
}
