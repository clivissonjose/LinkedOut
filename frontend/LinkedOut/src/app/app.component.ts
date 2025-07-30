import { Component, inject } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from './auth/auth.service';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "./header/header.component"

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, CommonModule, HeaderComponent],
  template: `<app-header></app-header>
    <router-outlet></router-outlet>`
})
export class AppComponent {
}
