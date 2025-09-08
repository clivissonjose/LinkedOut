import { Component, OnInit } from '@angular/core'; // Adicionar OnInit
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VacancyService } from './vacancy.service';
import { Router, ActivatedRoute } from '@angular/router'; // Adicionar ActivatedRoute

@Component({
  selector: 'app-vacancy-form',
  standalone: true,
  templateUrl: './vacancy-form.component.html',
  styleUrls: ['./vacancy-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class VacancyFormComponent implements OnInit { // Implementar OnInit
  form: FormGroup;
  enviado = false;
  erro = false;
  private companyId: number | null = null; // Propriedade para guardar o ID da empresa

  constructor(
    private fb: FormBuilder,
    private vacancyService: VacancyService,
    private router: Router,
    private route: ActivatedRoute // Injetar ActivatedRoute
  ) {
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
    // Pega o companyId da URL quando o componente é iniciado
    this.route.paramMap.subscribe(params => {
      const id = params.get('companyId');
      if (id) {
        this.companyId = +id; // O '+' converte a string para número
      } else {
        // Se não houver ID, redireciona para a lista de empresas
        alert('ID da empresa não fornecido.');
        this.router.navigate(['/empresas']);
      }
    });
  }

  onSubmit() {
    // Adiciona uma verificação para garantir que o companyId foi capturado
    if (this.form.invalid || !this.companyId) {
      return;
    }

    // Usa o companyId capturado da URL em vez do ID do usuário
    const payload = { ...this.form.value, companyId: this.companyId };

    this.vacancyService.createVacancy(payload).subscribe({
      next: () => {
        this.enviado = true;
        this.erro = false;
        this.form.reset();
        setTimeout(() => this.router.navigate(['/empresas']), 1500);
      },
      error: (err) => {
        console.error('Erro ao publicar vaga:', err);
        this.erro = true;
      }
    });
  }
}
