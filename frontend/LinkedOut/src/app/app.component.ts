import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
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
    title(title: any) {
        throw new Error('Method not implemented.');
    }
}
