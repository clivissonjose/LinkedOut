package com.va2es.backend.repositories;

import com.va2es.backend.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByStudentIdAndVacancyId(Long studentId, Long vacancyId);
    List<Application> findByVacancy_Company_Id(Long companyId);
}