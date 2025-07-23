import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <section>
      <div>
        <h2>Login</h2>
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <input type="email" formControlName="email" placeholder="Email" />
          <input type="password" formControlName="password" placeholder="Senha" />
          <button type="submit" [disabled]="loginForm.invalid">Entrar</button>
        </form>
        <p>Não tem conta? <a [routerLink]="['/register']">Cadastre-se</a></p>
      </div>
    </section>
  `
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required]
  });

  onSubmit() {
    const { email, password } = this.loginForm.value;
    this.authService.login(email, password)
      .then(success => {
        if (success) this.router.navigate(['/home']);
        else alert('Falha no login');
      });
  }
}
