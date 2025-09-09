package com.va2es.backend.dto;

import java.time.LocalDate;

public class StudentResponseDTO {
    private final Long id;
    private final String fullName;
    private final LocalDate birthDate;
    private final String cpf;
    private final String phone;
    private final String course;
    private final Integer currentPeriod;
    private final String academicSummary;
    private final String userEmail;

    // Construtor é privado e só pode ser chamado pelo Builder
    private StudentResponseDTO(Builder builder) {
        this.id = builder.id;
        this.fullName = builder.fullName;
        this.birthDate = builder.birthDate;
        this.cpf = builder.cpf;
        this.phone = builder.phone;
        this.course = builder.course;
        this.currentPeriod = builder.currentPeriod;
        this.academicSummary = builder.academicSummary;
        this.userEmail = builder.userEmail;
    }

    // Apenas Getters são públicos para manter o objeto imutável após a criação
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getCpf() { return cpf; }
    public String getPhone() { return phone; }
    public String getCourse() { return course; }
    public Integer getCurrentPeriod() { return currentPeriod; }
    public String getAcademicSummary() { return academicSummary; }
    public String getUserEmail() { return userEmail; }

    // Método estático para obter uma nova instância do Builder
    public static Builder builder() {
        return new Builder();
    }

    // Classe interna estática Builder
    public static final class Builder {
        private Long id;
        private String fullName;
        private LocalDate birthDate;
        private String cpf;
        private String phone;
        private String course;
        private Integer currentPeriod;
        private String academicSummary;
        private String userEmail;

        private Builder() {}

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder cpf(String cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder course(String course) {
            this.course = course;
            return this;
        }

        public Builder currentPeriod(Integer currentPeriod) {
            this.currentPeriod = currentPeriod;
            return this;
        }

        public Builder academicSummary(String academicSummary) {
            this.academicSummary = academicSummary;
            return this;
        }

        public Builder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        // Método final que constrói e retorna o objeto StudentResponseDTO
        public StudentResponseDTO build() {
            return new StudentResponseDTO(this);
        }
    }
}