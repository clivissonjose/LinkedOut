package com.va2es.backend.dto;

public class StudentPublicDTO {
    private String fullName;
    private int currentPeriod;
    private String phone;
    private String course;
    private String academicSummary;

    public StudentPublicDTO(String fullName, int currentPeriod, String phone, String course, String academicSummary) {
        this.fullName = fullName;
        this.currentPeriod = currentPeriod;
        this.phone = phone;
        this.course = course;
        this.academicSummary = academicSummary;
    }

    // --- Getters and Setters ---

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
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

    public String getAcademicSummary() {
        return academicSummary;
    }

    public void setAcademicSummary(String academicSummary) {
        this.academicSummary = academicSummary;
    }
}