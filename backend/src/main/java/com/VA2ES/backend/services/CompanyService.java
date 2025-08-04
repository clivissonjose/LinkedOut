package com.VA2ES.backend.services;

import com.VA2ES.backend.dto.CompanyRequestDTO;
import com.VA2ES.backend.dto.CompanyResponseDTO;
import com.VA2ES.backend.dto.StudentPublicDTO;

import com.VA2ES.backend.models.Company;
import com.VA2ES.backend.models.Student;
import com.VA2ES.backend.models.User;
import com.VA2ES.backend.repositories.CompanyRepository;
import com.VA2ES.backend.repositories.StudentRepository;

import com.VA2ES.backend.repositories.UserRepository;


import jakarta.persistence.EntityNotFoundException;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository empresaRepository;
    private final StudentRepository estudanteRepository;
    private final UserRepository userRepository;


    public CompanyService(CompanyRepository empresaRepository, StudentRepository estudanteRepository, UserRepository userRepository) {
        this.empresaRepository = empresaRepository;
        this.estudanteRepository = estudanteRepository;
        this.userRepository = userRepository;
    }

      public CompanyResponseDTO create(CompanyRequestDTO dto) {

        if(empresaRepository.findByCnpj(dto.cnpj).isPresent()){
            throw new IllegalArgumentException("Já existe uma empresa com este CNPJ.");
        }

        User representanteDaEmpresaId = userRepository.findById(dto.representanteDaEmpresaId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário representante não encontrado com id:" + dto.representanteDaEmpresaId));
        Company empresa = new Company();
        checkPermission(empresa.getId());
        empresa.setNomeDaEmpresa(dto.nomeDaEmpresa);
        empresa.setCnpj(dto.cnpj);
        empresa.setTelefone(dto.telefone);
        empresa.setAreaDeAtuacao(dto.areaDeAtuacao);
        empresa.setRepresentanteDaEmpresa(representanteDaEmpresaId);

        empresaRepository.save(empresa);
        return toDTO(empresa);
    }


    public CompanyResponseDTO findById(Long id) {
        checkPermission(id);
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada com id:" + id));
        return  toDTO(empresa);
    }

     public List<CompanyResponseDTO> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
        throw new AccessDeniedException("Usuário não autenticado.");
    }

    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    if (isAdmin) {
        return empresaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    Long userId = getAuthenticatedUserId(auth);

    return empresaRepository.findAll().stream()
            .filter(c -> c.getRepresentanteDaEmpresa() != null &&
                         c.getRepresentanteDaEmpresa().getId().equals(userId))
            .map(this::toDTO)
            .collect(Collectors.toList());
}


    public void deleteById(Long id) {
        checkPermission(id);
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        empresaRepository.delete(empresa);
    }


    public CompanyResponseDTO update(Long id, CompanyRequestDTO dadosAtualizados) {
        checkPermission(id);
        Company empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        empresaRepository.findByCnpj(dadosAtualizados.cnpj).ifPresent(empresaExistente -> {
        if (!empresaExistente.getId().equals(id)) {
            throw new IllegalArgumentException("Já existe uma empresa com este CNPJ.");
        }
        });

       User representanteDaEmpresaId = userRepository.findById(dadosAtualizados.representanteDaEmpresaId)
               .orElseThrow(() -> new EntityNotFoundException("Usuário representante não encontrado com id:" + dadosAtualizados.representanteDaEmpresaId));

        empresa.setNomeDaEmpresa(dadosAtualizados.nomeDaEmpresa);
        empresa.setCnpj(dadosAtualizados.cnpj);
        empresa.setTelefone(dadosAtualizados.telefone);
        empresa.setAreaDeAtuacao(dadosAtualizados.areaDeAtuacao);
        empresa.setRepresentanteDaEmpresa(representanteDaEmpresaId);

        empresaRepository.save(empresa);
        return  toDTO(empresa);
    }

    public List<CompanyResponseDTO> buscarPorNomeOuArea(String nome, String area) {
        List<Company> empresas = empresaRepository
            .findByNomeDaEmpresaContainingIgnoreCaseOrAreaDeAtuacaoContainingIgnoreCase(
                nome != null ? nome : "",
                area != null ? area : ""
            );

        return empresas.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
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


    // Buscar por nome do curso e intervalo de periodos
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

    /*métodos para apenas admin e representantes da empresa possam ter
    acesso as funcionalidades da Empresa*/
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


}
