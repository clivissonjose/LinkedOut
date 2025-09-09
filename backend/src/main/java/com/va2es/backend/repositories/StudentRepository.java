package com.va2es.backend.repositories;

import com.va2es.backend.models.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByCpf(String cpf);
    List<Student> findByCourseAndCurrentPeriodBetween(String course, int start, int end);
    List<Student> findByUserId(Long userId);
}
