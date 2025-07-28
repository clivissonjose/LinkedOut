package com.VA2ES.backend.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private LocalDate birthDate;
    private String cpf;
    private String phone;
    private String course;
    private String currentPeriod;

    @Column(length = 2000)
    private String academicSummary;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Construtor vazio
    public Student() {}

    // Construtor completo
    public Student(Long id, String fullName, LocalDate birthDate, String cpf, String phone, String course, String currentPeriod, String academicSummary, User user) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.cpf = cpf;
        this.phone = phone;
        this.course = course;
        this.currentPeriod = currentPeriod;
        this.academicSummary = academicSummary;
        this.user = user;
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

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public String getAcademicSummary() {
        return academicSummary;
    }

    public void setAcademicSummary(String academicSummary) {
        this.academicSummary = academicSummary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
