package com.va2es.backend.services;

import com.va2es.backend.dto.*;
import com.va2es.backend.models.*;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para garantir a consistência da transação
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository empresaRepository;
    private final StudentRepository estudanteRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;

    public CompanyService(CompanyRepository empresaRepository, StudentRepository estudanteRepository, UserRepository userRepository, ApplicationRepository applicationRepository, VacancyRepository vacancyRepository) {
        this.empresaRepository = empresaRepository;
        this.estudanteRepository = estudanteRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
    }

    @Transactional
    public CompanyResponseDTO create(CompanyRequestDTO dto) {
        if(empresaRepository.findByCnpj(dto.cnpj).isPresent()){
            throw new IllegalArgumentException("Já existe uma empresa com este CNPJ.");
        }

        User representante = userRepository.findById(dto.representanteDaEmpresaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário representante não encontrado com id:" + dto.representanteDaEmpresaId));

        // Se o usuário for USER ou STUDENT, promove para GESTOR. Não altera se for ADMIN.
        if (representante.getRole() == UserRole.USER || representante.getRole() == UserRole.STUDENT) {
            representante.setRole(UserRole.GESTOR);
            userRepository.save(representante);
        }

        Company empresa = new Company();
        empresa.setNomeDaEmpresa(dto.nomeDaEmpresa);
        empresa.setCnpj(dto.cnpj);
        empresa.setTelefone(dto.telefone);
        empresa.setAreaDeAtuacao(dto.areaDeAtuacao);
        empresa.setRepresentanteDaEmpresa(representante);

        empresaRepository.save(empresa);
        return toDTO(empresa);
    }

    public CompanyResponseDTO findById(Long id) {
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com id:" + id));
        return toDTO(empresa);
    }

    public List<CompanyResponseDTO> findAll() {
        return empresaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        checkPermission(id);
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
        empresaRepository.delete(empresa);
    }

    @Transactional // Garante que todas as operações com o banco sejam consistentes
    public CompanyResponseDTO update(Long id, CompanyRequestDTO dadosAtualizados) {
        checkPermission(id);
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        empresaRepository.findByCnpj(dadosAtualizados.cnpj).ifPresent(empresaExistente -> {
            if (!empresaExistente.getId().equals(id)) {
                throw new IllegalArgumentException("Já existe uma empresa com este CNPJ.");
            }
        });

        User novoRepresentante = userRepository.findById(dadosAtualizados.representanteDaEmpresaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário representante não encontrado com id:" + dadosAtualizados.representanteDaEmpresaId));

        User representanteAntigo = empresa.getRepresentanteDaEmpresa();

        // ---> LÓGICA DE ATUALIZAÇÃO E REMOÇÃO DE ROLE <---
        if (!representanteAntigo.getId().equals(novoRepresentante.getId())) {
            // Verifica se o representante antigo ainda gerencia outras empresas
            List<Company> outrasEmpresas = empresaRepository.findByRepresentanteDaEmpresa_Id(representanteAntigo.getId());
            // Se esta for a única empresa que ele gerenciava, remove a role de GESTOR
            if (outrasEmpresas.size() <= 1 && representanteAntigo.getRole() == UserRole.GESTOR) {
                // Verifica se o usuário também tem perfil de estudante
                boolean isStudent = estudanteRepository.findByUserId(representanteAntigo.getId()).size() > 0;
                representanteAntigo.setRole(isStudent ? UserRole.STUDENT : UserRole.USER);
                userRepository.save(representanteAntigo);
            }
        }

        // Promove o novo representante se necessário
        if (novoRepresentante.getRole() == UserRole.USER || novoRepresentante.getRole() == UserRole.STUDENT) {
            novoRepresentante.setRole(UserRole.GESTOR);
            userRepository.save(novoRepresentante);
        }

        empresa.setNomeDaEmpresa(dadosAtualizados.nomeDaEmpresa);
        empresa.setCnpj(dadosAtualizados.cnpj);
        empresa.setTelefone(dadosAtualizados.telefone);
        empresa.setAreaDeAtuacao(dadosAtualizados.areaDeAtuacao);
        empresa.setRepresentanteDaEmpresa(novoRepresentante);

        empresaRepository.save(empresa);
        return toDTO(empresa);
    }

    public List<CompanyResponseDTO> buscarPorNomeOuArea(String nome, String area) {
        List<Company> empresas = empresaRepository
                .findByNomeDaEmpresaContainingIgnoreCaseOrAreaDeAtuacaoContainingIgnoreCase(
                        nome != null ? nome : "",
                        area != null ? area : ""
                );
        return empresas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private CompanyResponseDTO toDTO(Company empresa) {
        return new CompanyResponseDTO(
                empresa.getId(),
                empresa.getNomeDaEmpresa(),
                empresa.getTelefone(),
                empresa.getCnpj(),
                empresa.getAreaDeAtuacao(),
                empresa.getRepresentanteDaEmpresa().getId(),
                empresa.getRepresentanteDaEmpresa().getNome()
        );
    }

    public List<StudentPublicDTO> filtroEstudantesPorAreaEPeriodo(String course, int periodMin, int periodMax){
        if (course == null || course.trim().isEmpty()) {
            throw new IllegalArgumentException("O curso deve ser informado.");
        }
        List<Student> estudantes = this.estudanteRepository.findByCourseAndCurrentPeriodBetween(course, periodMin, periodMax);
        if (estudantes.isEmpty()) {
            throw new EntityNotFoundException("Nenhum estudante encontrado para os critérios informados.");
        }
        return estudantes.stream()
                .map(estudante -> new StudentPublicDTO(
                        estudante.getFullName(),
                        estudante.getCurrentPeriod(),
                        estudante.getPhone(),
                        estudante.getCourse(),
                        estudante.getAcademicSummary()
                ))
                .collect(Collectors.toList());
    }

    private void checkPermission(Long companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Acesso negado: usuário não autenticado.");
        }
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return;

        if (companyId == null) {
            throw new AccessDeniedException("Acesso negado: operação inválida.");
        }

        Long userId = getAuthenticatedUserId(auth);
        if (!isOwner(companyId, userId)) {
            throw new AccessDeniedException("Acesso negado: apenas admin ou representante da empresa podem executar esta ação.");
        }
    }

    private void checkOwnershipPermission(Long companyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Acesso negado: usuário não autenticado.");
        }
        if (companyId == null) {
            throw new AccessDeniedException("Acesso negado: operação inválida.");
        }
        Long userId = getAuthenticatedUserId(auth);
        if (!isOwner(companyId, userId)) {
            throw new AccessDeniedException("Acesso negado: apenas o representante da empresa pode executar esta ação.");
        }
    }

    private boolean isOwner(Long companyId, Long userId) {
        Company empresa = empresaRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com id: " + companyId));
        User representante = empresa.getRepresentanteDaEmpresa();
        return representante != null && representante.getId().equals(userId);
    }

    private Long getAuthenticatedUserId(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return user.getId();
    }


    public List<VacancyWithApplicantsDTO> getApplicationsForCompany(Long companyId) {
        checkOwnershipPermission(companyId);

        List<Vacancy> vacancies = vacancyRepository.findByCompanyId(companyId);
        List<VacancyWithApplicantsDTO> result = new ArrayList<>();

        for (Vacancy vacancy : vacancies) {
            List<Application> applications = applicationRepository.findByVacancy_Id(vacancy.getId());

            List<ApplicantDTO> applicants = applications.stream()
                    .map(app -> new ApplicantDTO(
                            app.getStudent().getId(),
                            app.getStudent().getFullName(),
                            app.getStudent().getCourse(),
                            app.getApplicationDate()))
                    .collect(Collectors.toList());

            result.add(new VacancyWithApplicantsDTO(vacancy.getId(), vacancy.getTitulo(), applicants));
        }

        return result;
    }

    private ApplicationForCompanyDTO toApplicationDTO(Application app) {
        return new ApplicationForCompanyDTO(
                app.getId(),
                app.getApplicationDate(),
                app.getVacancy().getId(),
                app.getVacancy().getTitulo(),
                app.getStudent().getId(),
                app.getStudent().getFullName(),
                app.getStudent().getCourse()
        );
    }
}