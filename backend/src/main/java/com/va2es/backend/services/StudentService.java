package com.va2es.backend.services;

import com.va2es.backend.dto.StudentRequestDTO;
import com.va2es.backend.dto.StudentResponseDTO;
import com.va2es.backend.models.Application;
import com.va2es.backend.models.Student;
import com.va2es.backend.models.User;
import com.va2es.backend.models.Vacancy;
import com.va2es.backend.models.enums.UserRole;
import com.va2es.backend.repositories.ApplicationRepository;
import com.va2es.backend.repositories.StudentRepository;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final VacancyRepository vacancyRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    StudentService(StudentRepository studentRepository, UserRepository userRepository, VacancyRepository vacancyRepository, ApplicationRepository applicationRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.vacancyRepository = vacancyRepository;
        this.applicationRepository = applicationRepository;
    }

    public StudentResponseDTO create(StudentRequestDTO dto) {
        if (studentRepository.existsByCpf(dto.cpf)) {
            throw new IllegalArgumentException("Já existe um estudante com este CPF.");
        }

        User user = userRepository.findById(dto.userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.STUDENT);
            userRepository.save(user);
        }

        Student student = new Student(null,
                dto.fullName,
                dto.birthDate,
                dto.cpf,
                dto.phone,
                dto.course,
                dto.currentPeriod,
                dto.academicSummary,
                user);

        studentRepository.save(student);
        return toDTO(student);
    }

    public List<StudentResponseDTO> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return studentRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }

        Long userId = ((User) auth.getPrincipal()).getId();
        return studentRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não foi encontrado"));
        return toDTO(student);
    }

    public StudentResponseDTO update(Long id, StudentRequestDTO dto) {
        checkPermission(id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));

        student.setFullName(dto.fullName);
        student.setBirthDate(dto.birthDate);
        student.setCpf(dto.cpf);
        student.setPhone(dto.phone);
        student.setCourse(dto.course);
        student.setCurrentPeriod(dto.currentPeriod);
        student.setAcademicSummary(dto.academicSummary);

        studentRepository.save(student);
        return toDTO(student);
    }

    public void delete(Long id) {
        checkPermission(id);
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Estudante não encontrado");
        }
        studentRepository.deleteById(id);
    }

    private void checkPermission(Long studentId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Usuário não autenticado.");
        }

        var isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return;

        Long userId = ((User) auth.getPrincipal()).getId();

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        if (!student.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar o seu próprio cadastro.");
        }
    }

    public void applyToVacancy(Long studentId, Long vacancyId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));

        checkPermission(studentId);

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        if (applicationRepository.findByStudentIdAndVacancyId(studentId, vacancyId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já se candidatou para esta vaga.");
        }

        Application application = new Application(student, vacancy);
        applicationRepository.save(application);
    }

    private StudentResponseDTO toDTO(Student s) {
        return new StudentResponseDTO(
                s.getId(),
                s.getFullName(),
                s.getBirthDate(),
                s.getCpf(),
                s.getPhone(),
                s.getCourse(),
                s.getCurrentPeriod(),
                s.getAcademicSummary(),
                s.getUser().getId(), // Passando o ID do usuário para o frontend
                s.getUser().getEmail()
        );
    }
}