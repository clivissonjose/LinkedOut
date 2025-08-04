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

@Injectable({ providedIn: 'root' })
export class VacancyService {
  private readonly API = 'http://localhost:8080/vagas';

  constructor(private http: HttpClient, private auth: AuthService) {}

  createVacancy(vacancy: VacancyRequest): Observable<any> {
    const token = this.auth.getToken();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.post(`${this.API}/create`, vacancy, { headers });
  }
}
