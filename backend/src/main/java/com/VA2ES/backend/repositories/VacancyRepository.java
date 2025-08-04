package com.VA2ES.backend.repositories;

import com.VA2ES.backend.models.Vacancy;
import com.VA2ES.backend.models.enums.VacancyType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
   List<Vacancy> findByAreaAndTipo(String area, VacancyType vacancyType ); 
   List<Vacancy> findByAreaAndTipoAndDataLimiteBetween(String area, VacancyType vacancyType, LocalDate dataInico, LocalDate dataFim);
   
}
