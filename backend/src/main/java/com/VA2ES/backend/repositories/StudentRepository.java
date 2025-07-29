package com.VA2ES.backend.repositories;

import com.VA2ES.backend.models.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByCpf(String cpf);
    List<Student> findByCourseAndCurrentPeriodBetween(String course, int start, int end);

}
