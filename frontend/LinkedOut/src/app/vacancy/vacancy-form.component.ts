import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VacancyService } from './vacancy.service';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-vacancy-form',
  standalone: true,
  templateUrl: './vacancy-form.component.html',
  styleUrls: ['./vacancy-form.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class VacancyFormComponent {
  form: FormGroup;
  enviado = false;
  erro = false;

  constructor(
    private fb: FormBuilder,
    private vacancyService: VacancyService,
    private authService: AuthService,
    private router: Router
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

  onSubmit() {
    if (this.form.invalid) return;

    const companyId = this.authService.getUserId();
    const payload = { ...this.form.value, companyId };

    this.vacancyService.createVacancy(payload).subscribe({
      next: () => {
        this.enviado = true;
        this.erro = false;
        this.form.reset();
        setTimeout(() => this.router.navigate(['/empresas']), 1500);
      },
      error: () => {
        this.erro = true;
      }
    });
  }
}
