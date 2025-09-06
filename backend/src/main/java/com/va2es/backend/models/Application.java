package com.va2es.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @Column(nullable = false)
    private LocalDateTime applicationDate;

    // Construtor vazio para o JPA
    public Application() {
    }

    // Construtor para facilitar a criação
    public Application(Student student, Vacancy vacancy) {
        this.student = student;
        this.vacancy = vacancy;
        this.applicationDate = LocalDateTime.now(); // Pega a data e hora atuais
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Vacancy getVacancy() { return vacancy; }
    public void setVacancy(Vacancy vacancy) { this.vacancy = vacancy; }
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }
}