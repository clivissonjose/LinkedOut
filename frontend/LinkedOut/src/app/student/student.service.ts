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
  usuarioId: number;
}

@Injectable({
  providedIn: 'root'
})
export class StudentService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080/students';

  private getHeaders() {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getEstudantes(): Observable<Estudante[]> {
    return this.http.get<Estudante[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  createEstudante(estudante: any): Observable<Estudante> {
    return this.http.post<Estudante>(this.apiUrl, estudante, { headers: this.getHeaders() });
  }

  updateEstudante(id: number, estudante: any): Observable<Estudante> {
    return this.http.put<Estudante>(`${this.apiUrl}/${id}`, estudante, { headers: this.getHeaders() });
  }

  deleteEstudante(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}
