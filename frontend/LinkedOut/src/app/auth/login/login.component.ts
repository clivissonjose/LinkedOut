import { Component, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  template: `
    <section>
      <div>
        <h2>Login</h2>
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <input 
            type="email" 
            formControlName="email" 
            placeholder="Email" 
            [class.invalid]="loginForm.get('email')?.invalid && loginForm.get('email')?.touched"
          />
          <input 
            type="password" 
            formControlName="password" 
            placeholder="Senha" 
            [class.invalid]="loginForm.get('password')?.invalid && loginForm.get('password')?.touched"
          />
          <button type="submit" [disabled]="loginForm.invalid">Entrar</button>
        </form>
        <p>Não tem conta? <a [routerLink]="['/register']">Cadastre-se</a></p>
      </div>
    </section>
  `,
  styles: [`
    input.invalid {
      border-color: red;
    }
  `],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  async onSubmit(): Promise<void> {
    if (this.loginForm.invalid) return;

    const { email, password } = this.loginForm.value;

    const success = await this.authService.login(email, password);
    if (success) {
      this.router.navigate(['/home']);
    } else {
      alert('Falha no login. Verifique suas credenciais.');
    }
  }
}
