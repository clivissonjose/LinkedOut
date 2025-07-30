import { RouterModule, Router } from '@angular/router';
import { Component, inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  template: `
<header style="background-color:#6200ee; color:white; padding:1rem;">
  <nav>
    <a routerLink="/home" style="margin-right: 1rem; color:white; text-decoration:none;">Home</a>
    <a routerLink="/empresas" *ngIf="isAuthenticated()" style="margin-right: 1rem; color:white; text-decoration:none;">Empresas</a>
    <a routerLink="/login" style="margin-right: 1rem; color:white; text-decoration:none;">Login</a>
    <a 
      *ngIf="isAuthenticated()" 
      (click)="logout()" 
      style="color:white; text-decoration:none; cursor:pointer;"
    >Logout</a>
  </nav>
</header>

<router-outlet></router-outlet>

  `
})
export class HeaderComponent {

  private auth = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.auth.isAuthenticated();
  }
}

