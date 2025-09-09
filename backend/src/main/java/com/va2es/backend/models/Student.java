package com.va2es.backend.models;

import com.va2es.backend.validator.CPF;
import com.va2es.backend.validator.Phone;
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

    @CPF
    private String cpf;

    @Phone
    private String phone;

    private String course;
    private int currentPeriod;

    @Column(length = 2000)
    private String academicSummary;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // O construtor longo foi removido para resolver a issue do SonarCloud

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

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
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

    // Método estático para iniciar a construção do objeto
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
        private int currentPeriod;
        private String academicSummary;
        private User user;

        private Builder() {}

        public Builder id(Long id) { this.id = id; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder birthDate(LocalDate birthDate) { this.birthDate = birthDate; return this; }
        public Builder cpf(String cpf) { this.cpf = cpf; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder course(String course) { this.course = course; return this; }
        public Builder currentPeriod(int currentPeriod) { this.currentPeriod = currentPeriod; return this; }
        public Builder academicSummary(String academicSummary) { this.academicSummary = academicSummary; return this; }
        public Builder user(User user) { this.user = user; return this; }

        public Student build() {
            Student student = new Student();
            student.setId(this.id);
            student.setFullName(this.fullName);
            student.setBirthDate(this.birthDate);
            student.setCpf(this.cpf);
            student.setPhone(this.phone);
            student.setCourse(this.course);
            student.setCurrentPeriod(this.currentPeriod);
            student.setAcademicSummary(this.academicSummary);
            student.setUser(this.user);
            return student;
        }
    }
}