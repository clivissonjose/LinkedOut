import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section>
      <div>
        <h2>Cadastro</h2>
        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <input type="email" formControlName="email" placeholder="Digite seu email" required />
          <div *ngIf="registerForm.get('email')?.invalid && registerForm.get('email')?.touched">
            <p>Invalid email.</p>
          </div>
          <input type="text" formControlName="nome" placeholder="Nome" />
          <div *ngIf="registerForm.get('nome')?.invalid && registerForm.get('nome')?.touched">
            <p>Name is mandatory.</p>
          </div>
          <input type="password" formControlName="password" placeholder="Digite sua senha" required />
          <div *ngIf="registerForm.get('password')?.invalid && registerForm.get('password')?.touched">
            <p>Password is mandatory.</p>
          </div>
          <input type="password" formControlName="confirmPassword" placeholder="Confirme sua senha" required />
          <div *ngIf="registerForm.get('confirmPassword')?.invalid && registerForm.get('confirmPassword')?.touched">
            <p>Password confirmation is mandatory.</p>
          </div>
          <button type="submit" [disabled]="registerForm.invalid">Cadastrar</button>
        </form>
      </div>
    </section>
  `
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  registerForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    nome: ['', [Validators.required]],
    password: ['', [Validators.required]],
    confirmPassword: ['', [Validators.required]]
  }, { validators: this.passwordMatchValidator });

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return password === confirm ? null : { mismatch: true };
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const { email, password, nome } = this.registerForm.value;

      const newUser = { id: 0, email, password, nome, role: 'USER' };

      this.authService.register(newUser)
        .catch(err => console.error('Erro ao registrar:', err))
        .then(() => this.router.navigate(['/login']));
    }
  }
}
