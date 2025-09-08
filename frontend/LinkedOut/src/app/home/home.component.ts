import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Adicionado ReactiveFormsModule
  template: `
    <section class="home-container">
      <h2>Bem-vindo à tela inicial!</h2>
      <p>Use o menu de navegação acima para explorar o sistema.</p>

      <div class="role-changer">
        <h3>Ferramenta de Teste: Mudar Role</h3>
        <p>
          Para testar perfis diferentes, insira suas credenciais, escolha a nova role e clique em "Mudar Role".
          <strong>Você precisará fazer login novamente para que a mudança tenha efeito.</strong>
        </p>
        <form [formGroup]="roleForm" (ngSubmit)="changeRole()">
          <input type="email" formControlName="email" placeholder="Seu Email de Login">
          <input type="password" formControlName="password" placeholder="Sua Senha">
          <select formControlName="newRole">
            <option value="USER">USER (Padrão)</option>
            <option value="STUDENT">STUDENT (Perfil de Estudante)</option>
            <option value="GESTOR">GESTOR (Representante de Empresa)</option>
            <option value="ADMIN">ADMIN (Administrador)</option>
          </select>
          <button type="submit" [disabled]="roleForm.invalid">Mudar Role</button>
        </form>
      </div>
    </section>
  `,
  styles: [`
    .home-container { padding: 2rem; text-align: center; }
    .role-changer {
      margin: 2rem auto;
      padding: 2rem;
      border: 1px solid #ccc;
      border-radius: 8px;
      max-width: 500px;
      background-color: #f9f9f9;
    }
    .role-changer form {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      margin-top: 1rem;
    }
    .role-changer input, .role-changer select, .role-changer button {
      padding: 0.75rem;
      font-size: 1rem;
      border-radius: 6px;
      border: 1px solid #ccc;
    }
    .role-changer button {
      background-color: #ffc107;
      color: black;
      cursor: pointer;
    }
    .role-changer button:disabled {
      background-color: #e0e0e0;
      cursor: not-allowed;
    }
  `]
})
export class HomeComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  roleForm: FormGroup;

  constructor() {
    this.roleForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      newRole: ['USER', Validators.required]
    });
  }

  changeRole() {
    if (this.roleForm.invalid) {
      alert('Por favor, preencha todos os campos corretamente.');
      return;
    }

    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const payload = this.roleForm.value;

    this.http.post('http://localhost:8080/users/change-role', payload, { headers })
      .subscribe({
        next: (response: any) => {
          alert(response.message);
          this.authService.logout();
          window.location.href = '/login'; // Força o redirecionamento para a tela de login
        },
        error: (err) => {
          console.error('Erro ao mudar a role:', err);
          alert('Erro ao mudar a role: ' + (err.error?.error || 'Verifique suas credenciais.'));
        }
      });
  }
}
