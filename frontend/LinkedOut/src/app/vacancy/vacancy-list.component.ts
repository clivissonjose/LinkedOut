import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VacancyService, VacancyResponse } from './vacancy.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { StudentService } from '../student/student.service';
import { Router } from '@angular/router';
// NOVOS IMPORTS
import { CompanyService, Empresa } from '../company/company.service';

@Component({
  selector: 'app-vacancy-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './vacancy-list.component.html',
  styleUrls: ['./vacancy-list.component.css']
})
export class VacancyListComponent implements OnInit {
  vagas: VacancyResponse[] = [];
  filtroTitulo = '';
  filtroDescricao = '';
  filtroArea = '';
  currentStudentId: number | null = null;
  appliedVacancyIds = new Set<number>();

  // NOVA PROPRIEDADE PARA ARMAZENAR AS EMPRESAS DO GESTOR
  managedCompanies: Empresa[] = [];

  public authService = inject(AuthService);
  private studentService = inject(StudentService);
  private vacancyService = inject(VacancyService);
  private router = inject(Router);
  // NOVO SERVIÇO INJETADO
  private companyService = inject(CompanyService);

  ngOnInit(): void {
    this.carregarVagas();
    this.carregarPerfilEstudante();
    // NOVA CHAMADA DE MÉTODO
    this.carregarEmpresasGerenciadas();
  }

  // NOVO MÉTODO PARA BUSCAR E FILTRAR AS EMPRESAS DO USUÁRIO
  carregarEmpresasGerenciadas(): void {
    // Apenas busca se o usuário for GESTOR ou ADMIN
    if (this.authService.hasRole('GESTOR') || this.authService.hasRole('ADMIN')) {
      const currentUserId = this.authService.getUserId();
      this.companyService.getEmpresas().subscribe({
        next: (allCompanies) => {
          // Filtra a lista de todas as empresas para manter apenas aquelas
          // cujo representante é o usuário logado.
          this.managedCompanies = allCompanies.filter(
            company => company.representanteDaEmpresaId === currentUserId
          );
        },
        error: (err) => console.error('Erro ao carregar empresas gerenciadas:', err)
      });
    }
  }

  carregarVagas(): void {
    this.vacancyService.getAllVacancies().subscribe({
      next: data => this.vagas = data,
      error: err => console.error('Erro ao buscar vagas:', err)
    });
  }

  carregarPerfilEstudante(): void {
    if (this.authService.hasRole('STUDENT')) {
      this.studentService.getStudentProfileForCurrentUser().subscribe({
        next: (perfil) => {
          if (perfil) {
            this.currentStudentId = perfil.id;
            this.carregarCandidaturas(perfil.id);
          }
        }
      });
    }
  }

  carregarCandidaturas(studentId: number): void {
    this.studentService.getStudentApplications(studentId).subscribe({
      next: (applications) => {
        if(applications && applications.length > 0) {
          const ids = applications.map(app => app.vacancyId);
          this.appliedVacancyIds = new Set(ids);
        }
      },
      error: (err) => {
        console.error('Erro ao carregar candidaturas do estudante:', err);
      }
    });
  }

  get vagasFiltradas() {
    return this.vagas.filter(vaga =>
      vaga.areaDeAtuacao.toLowerCase().includes(this.filtroArea.toLowerCase()) &&
      vaga.titulo.toLowerCase().includes(this.filtroTitulo.toLowerCase()) &&
      vaga.descricao.toLowerCase().includes(this.filtroDescricao.toLowerCase())
    );
  }

  candidatarSe(vacancyId: number): void {
    if (!this.currentStudentId) return;
    if (!confirm('Deseja realmente se candidatar para esta vaga?')) return;

    this.studentService.applyToVacancy(this.currentStudentId, vacancyId).subscribe({
      next: () => {
        alert('Candidatura realizada com sucesso!');
        this.appliedVacancyIds.add(vacancyId);
      },
      error: (err) => alert(`Erro: ${err.error?.message || 'Não foi possível se candidatar.'}`)
    });
  }

  jaCandidatado(vacancyId: number): boolean {
    return this.appliedVacancyIds.has(vacancyId);
  }

  // A função de navegar para a página de candidaturas permanece a mesma
  verCandidaturas(companyId: number): void {
    // CORREÇÃO: Navega para a rota correta que espera o ID da empresa.
    this.router.navigate(['/empresa', companyId, 'candidaturas']);
  }
}
