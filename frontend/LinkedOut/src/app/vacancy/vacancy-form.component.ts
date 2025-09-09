import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VacancyService } from './vacancy.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-vacancy-form',
  standalone: true,
  templateUrl: './vacancy-form.component.html',
  styleUrls: ['./vacancy-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class VacancyFormComponent implements OnInit {
  form: FormGroup;
  enviado = false;
  erro = false;
  private companyId: number | null = null;

  // Injeção de dependências moderna
  private fb = inject(FormBuilder);
  private vacancyService = inject(VacancyService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  constructor() {
    this.form = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      requisitos: ['', Validators.required],
      area: ['', Validators.required],
      beneficios: [''],
      tipo: ['', Validators.required],
      dataTermino: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Captura o 'companyId' da rota assim que o componente é inicializado
    this.route.paramMap.subscribe(params => {
      const id = params.get('companyId');
      if (id) {
        this.companyId = +id; // O '+' converte a string para número
      } else {
        // Medida de segurança: se não houver ID, redireciona para a página de empresas
        alert('ID da empresa não fornecido na URL.');
        this.router.navigate(['/empresas']);
      }
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios.');
      return;
    }

    // Garante que o companyId foi capturado antes de enviar
    if (!this.companyId) {
      alert('Erro: ID da empresa não encontrado. Não é possível criar a vaga.');
      return;
    }

    // Monta o payload para a API, incluindo o companyId
    const payload = { ...this.form.value, companyId: this.companyId };

    this.vacancyService.createVacancy(payload).subscribe({
      next: () => {
        this.enviado = true;
        this.erro = false;
        alert('Vaga publicada com sucesso!');
        this.form.reset();
        // Redireciona para a lista de vagas após um breve intervalo
        setTimeout(() => this.router.navigate(['/vagas']), 1500);
      },
      error: (err) => {
        console.error('Erro ao publicar vaga:', err);
        this.erro = true;
        alert(`Ocorreu um erro: ${err.error?.message || 'Não foi possível publicar a vaga.'}`);
      }
    });
  }
}
