package com.va2es.backend.services;

import com.va2es.backend.dto.StudentRequestDTO;
import com.va2es.backend.dto.StudentResponseDTO;
import com.va2es.backend.models.Student;
import com.va2es.backend.models.User;
import com.va2es.backend.repositories.StudentRepository;
import com.va2es.backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    private UserRepository userRepository;

    StudentService(StudentRepository studentRepository, UserRepository userRepository){
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public StudentResponseDTO create(StudentRequestDTO dto) {
        // valid cpf
        if (studentRepository.existsByCpf(dto.cpf)) {
            throw new IllegalArgumentException("Já existe um estudante com este CPF.");
        }

        // find user and check if don´t exist
        User user = userRepository.findById(dto.userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // create new student
        Student student = new Student(null,
                dto.fullName,
                dto.birthDate,
                dto.cpf,
                dto.phone,
                dto.course,
                dto.currentPeriod,
                dto.academicSummary,
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
                .collect(Collectors.toList());
    }

    public StudentResponseDTO findById(Long id) {
//        checkPermission(id); caso achar necessario
        // find user and check if don´t exist
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não foi encontrado"));
        return toDTO(student);
    }

    public StudentResponseDTO update(Long id, StudentRequestDTO dto) {
        checkPermission(id);
        // find user and check if don´t exist
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));

        // update all filds one by one
        if (dto.fullName != null) student.setFullName(dto.fullName);
        if (dto.birthDate != null) student.setBirthDate(dto.birthDate);
        if (dto.cpf != null) student.setCpf(dto.cpf);
        if (dto.phone != null) student.setPhone(dto.phone);
        if (dto.course != null) student.setCourse(dto.course);
        if (dto.currentPeriod != null) student.setCurrentPeriod(dto.currentPeriod);
        if (dto.academicSummary != null) student.setAcademicSummary(dto.academicSummary);

        // valid user
        if (dto.userId != null && !dto.userId.equals(student.getUser().getId())) {
            User user = userRepository.findById(dto.userId)
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
            throw new EntityNotFoundException("Estudante não encontrado");
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
                .orElseThrow(() -> new EntityNotFoundException("Estudante não encontrado"));
        if (!student.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Acesso negado: você só pode acessar o seu próprio cadastro.");
        }
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
