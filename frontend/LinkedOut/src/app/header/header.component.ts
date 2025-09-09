import { RouterModule, Router } from '@angular/router';
import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  // Template foi refatorado para maior clareza e controle de acesso
  template: `
    <header class="main-header">
      <nav class="main-nav">
        <a routerLink="/home" *ngIf="isAuthenticated()">Home</a>
        <a routerLink="/vagas" *ngIf="isAuthenticated()">Vagas</a>

        <a routerLink="/empresas" *ngIf="isAuthenticated()">Empresas</a>

        <a routerLink="/estudantes" *ngIf="isAuthenticated()">Estudantes</a>
      </nav>
      <nav class="user-nav">
        <a *ngIf="!isAuthenticated()" routerLink="/login">Login</a>
        <a *ngIf="isAuthenticated()" (click)="logout()" class="logout-link">Logout</a>
      </nav>
    </header>
  `,
  // Estilos foram adicionados para melhorar a aparência e remover a necessidade de style inline
  styles: [`
    .main-header {
      background-color: #6200ee;
      color: white;
      padding: 1rem 2rem;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }
    .main-nav, .user-nav {
      display: flex;
      gap: 1.5rem;
      align-items: center;
    }
    a {
      color: white;
      text-decoration: none;
      font-weight: 500;
      padding: 0.5rem;
      border-radius: 4px;
      transition: background-color 0.2s ease-in-out;
    }
    a:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }
    .logout-link {
      cursor: pointer;
    }
  `]
})
export class HeaderComponent {
  auth = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.auth.isAuthenticated();
  }

  // Método hasRole agora é público para ser acessado pelo template
  public hasRole(role: string): boolean {
    return this.auth.hasRole(role);
  }
}
