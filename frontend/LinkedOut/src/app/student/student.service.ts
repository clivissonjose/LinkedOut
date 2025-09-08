import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

export interface Estudante {
  id: number;
  nomeCompleto: string;
  dataNascimento: string;
  cpf: string;
  telefone?: string;
  curso: string;
  periodoAtual: number;
  resumoAcademico: string;
  userId: number; // userId é necessário para a lógica no frontend
  userEmail: string;
}

// Interface para a lista de usuários que o admin vai ver
export interface Usuario {
  id: number;
  nome: string;
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080';

  private getHeaders() {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  // Novo método para buscar usuários
  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/users`, { headers: this.getHeaders() });
  }

  getEstudantes(): Observable<Estudante[]> {
    return this.http.get<Estudante[]>(`${this.apiUrl}/students`, { headers: this.getHeaders() });
  }

  createEstudante(estudante: any): Observable<Estudante> {
    return this.http.post<Estudante>(`${this.apiUrl}/students`, estudante, { headers: this.getHeaders() });
  }

  updateEstudante(id: number, estudante: any): Observable<Estudante> {
    return this.http.put<Estudante>(`${this.apiUrl}/students/${id}`, estudante, { headers: this.getHeaders() });
  }

  deleteEstudante(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/students/${id}`, { headers: this.getHeaders() });
  }
}
