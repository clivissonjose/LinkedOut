package com.va2es.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class StudentRequestDTO {

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres.")
    public String fullName;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message= "A data de nascimento deve ser no passado.")
    public LocalDate birthDate;

    @NotNull(message = "O CPF é obrigatório.")
    public String cpf;

    @NotNull(message = "O telefone é obrigatório.")
    public String phone;

    public String course;
    public Integer currentPeriod;
    public String academicSummary;

    @NotNull(message = "O ID do usuário é obrigatório.")
    public Long userId;

    // Getters
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getCpf() { return cpf; }
    public String getPhone() { return phone; }
    public String getCourse() { return course; }
    public Integer getCurrentPeriod() { return currentPeriod; }
    public String getAcademicSummary() { return academicSummary; }
    public Long getUserId() { return userId; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCourse(String course) { this.course = course; }
    public void setCurrentPeriod(Integer currentPeriod) { this.currentPeriod = currentPeriod; }
    public void setAcademicSummary(String academicSummary) { this.academicSummary = academicSummary; }
    public void setUserId(Long userId) { this.userId = userId; }
}