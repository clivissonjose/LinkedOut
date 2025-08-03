package com.VA2ES.backend.repositories;

import com.VA2ES.backend.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
   List<Vacancy> findByArea(String area);
}
