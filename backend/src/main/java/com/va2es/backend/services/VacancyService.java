package com.va2es.backend.services;

import com.va2es.backend.dto.VacancyRequestDTO;
import com.va2es.backend.dto.VacancyResponseDTO;
import com.va2es.backend.dto.VacancyUpdateDTO;
import com.va2es.backend.models.Company;
import com.va2es.backend.models.User;
import com.va2es.backend.models.Vacancy;
import com.va2es.backend.models.enums.VacancyType;
import com.va2es.backend.repositories.CompanyRepository;
import com.va2es.backend.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VacancyService {
    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;

    public VacancyService(VacancyRepository vacancyRepository, CompanyRepository companyRepository) {
        this.vacancyRepository = vacancyRepository;
        this.companyRepository = companyRepository;
    }

    public VacancyResponseDTO createVacancy(VacancyRequestDTO vacancyRequestDTO) {
        Company empresa = companyRepository.findById(vacancyRequestDTO.getCompanyId()).
                orElseThrow(()-> new EntityNotFoundException("Empresa não encontrada"));
        checkPermission(empresa.getId());

        Vacancy vaga = new Vacancy();
        vaga.setDataPublicacao(LocalDate.now());
        vaga.setDataLimite(vacancyRequestDTO.getDataTermino());
        vaga.setTitulo(vacancyRequestDTO.getTitulo());
        vaga.setDescricao(vacancyRequestDTO.getDescricao());
        vaga.setRequisitos(vacancyRequestDTO.getRequisitos());
        vaga.setArea(vacancyRequestDTO.getArea());
        vaga.setBeneficios(vacancyRequestDTO.getBeneficios());
        vaga.setTipo(vacancyRequestDTO.getTipo());
        vaga.setCompany(empresa);

        Vacancy vagaSalva = vacancyRepository.save(vaga);
        return toDTO(vagaSalva);
    }

    public VacancyResponseDTO findById(Long id){
        Vacancy vaga = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada."));
        return toDTO(vaga);
    }

    public List<VacancyResponseDTO> findAll(){
        return vacancyRepository.findAll().stream()
                .map(this::toDTO)
                .toList(); // Corrigido
    }

    public VacancyResponseDTO update(Long id, VacancyUpdateDTO vacancyUpdateDTO){
        Vacancy vaga = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada."));
        checkPermission(vaga.getCompany().getId());

        vaga.setDataLimite(vacancyUpdateDTO.getDataTermino());
        vaga.setTitulo(vacancyUpdateDTO.getTitulo());
        vaga.setDescricao(vacancyUpdateDTO.getDescricao());
        vaga.setRequisitos(vacancyUpdateDTO.getRequisitos());
        vaga.setArea(vacancyUpdateDTO.getArea());
        vaga.setBeneficios(vacancyUpdateDTO.getBeneficios());
        vaga.setTipo(vacancyUpdateDTO.getTipo());

        Vacancy vagaSalva = vacancyRepository.save(vaga);
        return toDTO(vagaSalva);
    }

    public void delete(Long id){
        Vacancy vaga = vacancyRepository.findById(id).
                orElseThrow(()-> new EntityNotFoundException("Vaga não encontrada"));
        checkPermission(vaga.getCompany().getId());
        vacancyRepository.delete(vaga);
    }

    public void checkPermission(Long companyId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Acesso negado: usuário não autenticado.");
        }

        Company empresa = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        Long userIdLogado = ((User) auth.getPrincipal()).getId();
        Long idDoRepresentante = empresa.getRepresentanteDaEmpresa().getId();
        if (!idDoRepresentante.equals(userIdLogado)) {
            throw new AccessDeniedException("Acesso negado: apenas o representante da empresa pode executar esta ação.");
        }
    }

    private VacancyResponseDTO toDTO(Vacancy vaga){
        // Usando o Builder que já implementamos no VacancyResponseDTO
        return VacancyResponseDTO.builder()
                .id(vaga.getId())
                .titulo(vaga.getTitulo())
                .descricao(vaga.getDescricao())
                .requisitos(vaga.getRequisitos())
                .areaDeAtuacao(vaga.getArea())
                .beneficios(vaga.getBeneficios())
                .tipo(vaga.getTipo())
                .idDaEmpresa(vaga.getCompany().getId())
                .nomeDaEmpresa(vaga.getCompany().getNomeDaEmpresa())
                .dataPublicacao(vaga.getDataPublicacao())
                .dataTermino(vaga.getDataLimite())
                .build();
    }

    // Pega todas as vagas de acordo com a area e a tipo da vaga
    public List<VacancyResponseDTO> filterByAreaAndTipo(String area, VacancyType vacancyType){
        List<Vacancy> filteredVacancies = vacancyRepository.findByAreaAndTipo(area, vacancyType);
        return filteredVacancies.stream()
                .map(this::toDTO)
                .toList(); // Corrigido
    }

    // Pega as vagas que vão se encerrar durante um periodo
    public List<VacancyResponseDTO> filterByAreaAndTipoAndPeriodo(String area, VacancyType vacancyType, LocalDate inicio, LocalDate fim){
        List<Vacancy> filteredVacancies = vacancyRepository.findByAreaAndTipoAndDataLimiteBetween(area, vacancyType, inicio, fim);
        return filteredVacancies.stream()
                .map(this::toDTO)
                .toList(); // Corrigido
    }
}