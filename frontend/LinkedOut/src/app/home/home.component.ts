import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `<h2>Bem-vindo à tela inicial!</h2>`
})
export class HomeComponent {}
