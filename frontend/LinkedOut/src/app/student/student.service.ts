import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
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
  userId: number;
  userEmail: string;
}

export interface Usuario {
  id: number;
  nome: string;
}

// ---> NOVA INTERFACE PARA A RESPOSTA DA API <---
export interface ApplicationForStudent {
  applicationId: number;
  vacancyId: number;
  vacancyTitle: string;
  companyName: string;
  applicationDate: string;
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

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/users`, { headers: this.getHeaders() });
  }

  getEstudantes(): Observable<Estudante[]> {
    return this.http.get<Estudante[]>(`${this.apiUrl}/students`, { headers: this.getHeaders() });
  }

  getStudentProfileForCurrentUser(): Observable<Estudante | null> {
    return this.getEstudantes().pipe(
      map(estudantes => (estudantes && estudantes.length > 0) ? estudantes[0] : null)
    );
  }

  applyToVacancy(studentId: number, vacancyId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.post<void>(`${this.apiUrl}/students/${studentId}/apply/${vacancyId}`, {}, { headers });
  }

  // ---> NOVO MÉTODO ADICIONADO AQUI <---
  // Busca as candidaturas de um estudante.
  getStudentApplications(studentId: number): Observable<ApplicationForStudent[]> {
    const headers = this.getHeaders();
    return this.http.get<ApplicationForStudent[]>(`${this.apiUrl}/students/${studentId}/applications`, { headers });
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
