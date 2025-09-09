package com.va2es.backend.services;

import com.va2es.backend.dto.StudentRequestDTO;
import com.va2es.backend.dto.StudentResponseDTO;
import com.va2es.backend.models.Application;
import com.va2es.backend.models.Student;
import com.va2es.backend.models.User;
import com.va2es.backend.models.Vacancy;
import com.va2es.backend.repositories.ApplicationRepository;
import com.va2es.backend.repositories.StudentRepository;
import com.va2es.backend.repositories.UserRepository;
import com.va2es.backend.repositories.VacancyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    private static final String STUDENT_NOT_FOUND_MESSAGE = "Estudante não encontrado";
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
        // valid cpf - Antes: dto.cpf | Agora: dto.getCpf()
        if (studentRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um estudante com este CPF.");
        }

        // find user and check if don´t exist - Antes: dto.userId | Agora: dto.getUserId()
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // create new student - Usando getters para todos os campos do DTO
        Student student = new Student(null,
                dto.getFullName(),
                dto.getBirthDate(),
                dto.getCpf(),
                dto.getPhone(),
                dto.getCourse(),
                dto.getCurrentPeriod(),
                dto.getAcademicSummary(),
                user);
        // save strudent
        studentRepository.save(student);

        //return dto to request
        return toDTO(student);
    }

    public List<StudentResponseDTO> findAll() {
        // all user in data base
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public StudentResponseDTO findById(Long id) {
        // find user and check if don´t exist
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não foi encontrado"));
        return toDTO(student);
    }

    public StudentResponseDTO update(Long id, StudentRequestDTO dto) {
        checkPermission(id);
        // find user and check if don´t exist
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_MESSAGE));

        // update all filds one by one - Usando getters para todos os campos do DTO
        if (dto.getFullName() != null) student.setFullName(dto.getFullName());
        if (dto.getBirthDate() != null) student.setBirthDate(dto.getBirthDate());
        if (dto.getCpf() != null) student.setCpf(dto.getCpf());
        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getCourse() != null) student.setCourse(dto.getCourse());
        if (dto.getCurrentPeriod() != null) student.setCurrentPeriod(dto.getCurrentPeriod());
        if (dto.getAcademicSummary() != null) student.setAcademicSummary(dto.getAcademicSummary());

        // valid user
        if (dto.getUserId() != null && !dto.getUserId().equals(student.getUser().getId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            student.setUser(user);
        }

        // save in data base
        studentRepository.save(student);
        // response to request
        return toDTO(student);
    }

    public void delete(Long id) {
        checkPermission(id);
        // find user or check if don´t exist
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException(STUDENT_NOT_FOUND_MESSAGE);
        }
        //delete in data base
        studentRepository.deleteById(id);
        // do not return cotent
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
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_MESSAGE));
        if (!student.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar o seu próprio cadastro.");
        }
    }

    public void applyToVacancy(Long studentId, Long vacancyId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(STUDENT_NOT_FOUND_MESSAGE));

        checkPermission(studentId);

        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        if (applicationRepository.findByStudentIdAndVacancyId(studentId, vacancyId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já se candidatou para esta vaga.");
        }

        Application application = new Application(student, vacancy);
        applicationRepository.save(application);
    }

    // convert objet to dto to request
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
                s.getUser().getEmail()
        );
    }
}