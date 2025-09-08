import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';

export interface VacancyRequest {
  titulo: string;
  descricao: string;
  requisitos: string;
  area: string;
  beneficios: string;
  tipo: 'ESTAGIO' | 'TRAINEE' | 'EMPREGO';
  companyId: number;
  dataTermino: string;
}

export interface VacancyResponse {
  id: number;
  titulo: string;
  descricao: string;
  requisitos: string;
  areaDeAtuacao: string;
  beneficios: string;
  tipo: 'ESTAGIO' | 'TRAINEE' | 'EMPREGO';
  idDaEmpresa: number;
  nomeDaEmpresa: string;
  representanteId: number;
  dataPublicacao: string;
  dataTermino: string;
}

@Injectable({ providedIn: 'root' })
export class VacancyService {
  private readonly API = 'http://localhost:8080/vagas';

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders() {
    const token = this.auth.getToken();
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  createVacancy(vacancy: VacancyRequest): Observable<any> {
    return this.http.post(`${this.API}/create`, vacancy, { headers: this.getHeaders() });
  }

  getAllVacancies(): Observable<VacancyResponse[]> {
    return this.http.get<VacancyResponse[]>(`${this.API}/list`, { headers: this.getHeaders() });
  }

  // ---> NOVO MÉTODO ADICIONADO <---
  deleteVacancy(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/delete/${id}`, { headers: this.getHeaders() });
  }
}
