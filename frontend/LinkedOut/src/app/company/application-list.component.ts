import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { CompanyService, Empresa, VacancyWithApplicants } from './company.service';

@Component({
  selector: 'app-application-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private companyService = inject(CompanyService);

  companyId: number | null = null;
  company: Empresa | null = null;
  vacanciesWithApplicants: VacancyWithApplicants[] = [];
  isLoading = true;

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id) {
          this.companyId = +id;
          this.loadCompanyDetails();
          return this.companyService.getApplicationsForCompany(this.companyId);
        }
        this.router.navigate(['/empresas']);
        return of([]);
      })
    ).subscribe({
      next: (data) => {
        // A atribuição agora é direta, sem necessidade de agrupar
        this.vacanciesWithApplicants = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar candidaturas:', err);
        const errorMessage = err.error?.message || 'Você pode não ter permissão para ver estas candidaturas.';
        alert(`Erro: ${errorMessage}`);
        this.router.navigate(['/vagas']);
        this.isLoading = false;
      }
    });
  }

  loadCompanyDetails(): void {
    if (!this.companyId) return;
    this.companyService.getCompanyById(this.companyId).subscribe({
      next: (empresa) => this.company = empresa,
      error: (err) => {
        console.error('Erro ao carregar detalhes da empresa:', err);
        this.company = null;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/vagas']);
  }
}
