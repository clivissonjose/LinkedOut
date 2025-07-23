import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section>
      <div>
        <h2>Cadastro</h2>
        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
          <input type="email" formControlName="email" placeholder="Email" />
          <input type="password" formControlName="password" placeholder="Senha" />
          <input type="password" formControlName="confirmPassword" placeholder="Confirme a senha" />
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
      const { email, password } = this.registerForm.value;
      this.authService.register({ id: 0, email, password, role: 'user' })
        .then(() => this.router.navigate(['/login']));
    }
  }
}
