import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { HomeComponent } from './home/home.component';
import { UnauthorizedComponent } from './auth/unauthorized/unauthorized.component';
import { CompanyComponent } from './company/company.component';
import { authGuard } from './auth/auth.guard';
import {StudentComponent} from './student/student.component';
import {VacancyFormComponent} from './vacancy/vacancy-form.component';
import {VacancyListComponent} from './vacancy/vacancy-list-component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: 'unauthorized', component: UnauthorizedComponent },
  { path: 'empresas', component: CompanyComponent },
  {path: 'estudantes', component: StudentComponent},
  { path: 'vaga/publicar', component: VacancyFormComponent, canActivate: [authGuard] },
  { path: 'vagas', component: VacancyListComponent },
  { path: '**', redirectTo: 'login' }

];
