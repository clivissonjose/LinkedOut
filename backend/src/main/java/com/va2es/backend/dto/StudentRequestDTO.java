package com.va2es.backend.dto;

import com.va2es.backend.validator.CPF;
import com.va2es.backend.validator.Phone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class StudentRequestDTO {

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome completo deve ter entre 3 e 100 caracteres.")
    private String fullName;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate birthDate;

    @NotNull(message = "O CPF é obrigatório.")
    @CPF
    private String cpf;

    @NotNull(message = "O telefone é obrigatório.")
    @Phone
    private String phone;

    private String course;

    private Integer currentPeriod;

    private String academicSummary;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long userId;

    // --- Getters e Setters ---

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}