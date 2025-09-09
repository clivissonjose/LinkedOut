import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VacancyService, VacancyResponse } from './vacancy.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { StudentService } from '../student/student.service';
import { Router } from '@angular/router';

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

  public authService = inject(AuthService);
  private studentService = inject(StudentService);
  private vacancyService = inject(VacancyService);
  private router = inject(Router);

  ngOnInit(): void {
    this.carregarVagas();
    if (this.authService.hasRole('STUDENT')) {
      this.carregarPerfilEstudante();
    }
  }

  carregarVagas(): void {
    this.vacancyService.getAllVacancies().subscribe({
      next: data => this.vagas = data,
      error: err => console.error('Erro ao buscar vagas:', err)
    });
  }

  carregarPerfilEstudante(): void {
    this.studentService.getStudentProfileForCurrentUser().subscribe({
      next: (perfil) => {
        if (perfil) {
          this.currentStudentId = perfil.id;
          this.carregarCandidaturas(perfil.id);
        }
      }
    });
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

  // ---> NOVO MÉTODO <---
  // Verifica se o usuário logado é o gestor da vaga ou um admin
  isManagerOf(vacancy: VacancyResponse): boolean {
    const userId = this.authService.getUserId();
    return this.authService.hasRole('ADMIN') || userId === vacancy.representanteId;
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

  verCandidaturas(companyId: number): void {
    this.router.navigate(['/empresa', companyId, 'candidaturas']);
  }

  // ---> NOVO MÉTODO <---
  excluirVaga(vacancyId: number): void {
    if (!confirm('Tem certeza que deseja excluir esta vaga? Esta ação não pode ser desfeita.')) {
      return;
    }

    this.vacancyService.deleteVacancy(vacancyId).subscribe({
      next: () => {
        alert('Vaga excluída com sucesso!');
        // Remove a vaga da lista local para atualizar a UI instantaneamente
        this.vagas = this.vagas.filter(v => v.id !== vacancyId);
      },
      error: (err) => {
        console.error('Erro ao excluir vaga:', err);
        alert('Erro ao excluir vaga.');
      }
    });
  }

  // ---> NOVO MÉTODO (PLACEHOLDER) <---
  editarVaga(vacancyId: number): void {
    // Futuramente, isso navegará para uma rota de edição, ex:
    // this.router.navigate(['/vagas/editar', vacancyId]);
    alert('Funcionalidade de edição a ser implementada.');
  }
}
