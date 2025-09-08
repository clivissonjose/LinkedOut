import { RouterModule, Router } from '@angular/router';
import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  template: `
    <header style="background-color:#6200ee; color:white; padding:1rem; display: flex; justify-content: space-between; align-items: center;">
      <nav>
        <a routerLink="/home" *ngIf="isAuthenticated()" style="margin-right: 1rem; color:white; text-decoration:none;">Home</a>
        <a routerLink="/vagas" *ngIf="isAuthenticated()" style="margin-right: 1rem; color:white; text-decoration:none;">Vagas</a>
        <a routerLink="/empresas" *ngIf="isAuthenticated()" style="margin-right: 1rem; color:white; text-decoration:none;">Empresas</a>
        <a routerLink="/estudantes" *ngIf="isAuthenticated()" style="margin-right: 1rem; color:white; text-decoration:none;">Estudantes</a>
      </nav>
      <nav>
        <a *ngIf="!isAuthenticated()" routerLink="/login" style="margin-right: 1rem; color:white; text-decoration:none;">Login</a>
        <a *ngIf="isAuthenticated()" (click)="logout()" style="color:white; text-decoration:none; cursor:pointer;">Logout</a>
      </nav>
    </header>
  `
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

  hasRole(role: string): boolean {
    return this.auth.hasRole(role);
  }
}
