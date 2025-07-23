import { Component, inject } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, CommonModule],
  template: `
    <header>
      <h1>LinkedOut</h1>
      <button *ngIf="isAuthenticated()" (click)="logout()">Logout</button>
    </header>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {
  title = 'LinkedOut';

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
