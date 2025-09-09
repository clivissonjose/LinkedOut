import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

export interface Usuario {
  id: number;
  nome: string;
}

export interface Empresa {
  id: number;
  nomeDaEmpresa: string;
  cnpj: string;
  telefone?: string;
  areaDeAtuacao: string;
  representanteDaEmpresaId: number;
  representanteDaEmpresaNome: string;
}

// ---> NOVA INTERFACE PARA O CANDIDATO <---
export interface Applicant {
  studentId: number;
  studentName: string;
  studentCourse: string;
  applicationDate: string;
}

// ---> NOVA INTERFACE PARA A VAGA COM SEUS CANDIDATOS <---
export interface VacancyWithApplicants {
  vacancyId: number;
  vacancyTitle: string;
  applicants: Applicant[];
}


@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private apiUrl = 'http://localhost:8080';

  private getHeaders() {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getCompanyById(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.apiUrl}/company/search/${id}`, { headers: this.getHeaders() });
  }

  // ---> TIPO DE RETORNO ATUALIZADO PARA USAR A NOVA INTERFACE <---
  getApplicationsForCompany(companyId: number): Observable<VacancyWithApplicants[]> {
    return this.http.get<VacancyWithApplicants[]>(`${this.apiUrl}/company/${companyId}/applications`, { headers: this.getHeaders() });
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(`${this.apiUrl}/users`, { headers: this.getHeaders() });
  }

  getEmpresas(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(`${this.apiUrl}/company/list`, { headers: this.getHeaders() });
  }

  createEmpresa(empresa: any): Observable<Empresa> {
    return this.http.post<Empresa>(`${this.apiUrl}/company/create`, empresa, { headers: this.getHeaders() });
  }

  updateEmpresa(id: number, empresa: any): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.apiUrl}/company/update/${id}`, empresa, { headers: this.getHeaders() });
  }

  deleteEmpresa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/company/delete/${id}`, { headers: this.getHeaders() });
  }
}
