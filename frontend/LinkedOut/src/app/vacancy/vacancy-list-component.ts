import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VacancyService, VacancyRequest } from './vacancy.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vacancy-list',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './vacancy-list.component.html',
  styleUrls: ['./vacancy-list.component.css']
})
export class VacancyListComponent implements OnInit {
  vagas: VacancyRequest[] = [];

  filtroCurso = '';
  filtroPeriodo: number | null = null;
  filtroLocalizacao = '';
  filtroArea = '';

  constructor(private vacancyService: VacancyService) {}

  ngOnInit(): void {
    this.vacancyService.getAllVacancies().subscribe({
      next: data => this.vagas = data,
      error: err => console.error('Erro ao buscar vagas:', err)
    });
  }

  get vagasFiltradas() {
    return this.vagas.filter(vaga =>
      vaga.area.toLowerCase().includes(this.filtroArea.toLowerCase()) &&
      vaga.titulo.toLowerCase().includes(this.filtroCurso.toLowerCase()) &&
      vaga.descricao.toLowerCase().includes(this.filtroLocalizacao.toLowerCase())
    );
  }
}
