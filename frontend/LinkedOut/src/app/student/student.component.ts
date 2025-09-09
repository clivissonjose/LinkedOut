import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';
import { FormsModule } from '@angular/forms';

interface Estudante {
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

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule, FormsModule],
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private authService = inject(AuthService);

  estudantes: Estudante[] = [];
  mostrarFormulario = false;
  estudanteEditando: Estudante | null = null;

  filtroCurso = '';
  periodoMin: number | null = null;
  periodoMax: number | null = null;

  formEstudante: FormGroup = this.fb.group({
    nomeCompleto: ['', Validators.required],
    dataNascimento: ['', Validators.required],
    cpf: ['', Validators.required],
    telefone: [''],
    curso: ['', Validators.required],
    periodoAtual: ['', [Validators.required, Validators.min(1)]],
    resumoAcademico: ['', Validators.required]
  });

  ngOnInit(): void {
    this.carregarEstudantes();
  }

  private getHeaders() {
    const token = this.authService.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  get estudantesFiltrados(): Estudante[] {
    return this.estudantes.filter(estudante => {
      const cursoOk = estudante.curso.toLowerCase().includes(this.filtroCurso.toLowerCase());
      const periodo = estudante.periodoAtual;
      const periodoMinOk = this.periodoMin === null || periodo >= this.periodoMin;
      const periodoMaxOk = this.periodoMax === null || periodo <= this.periodoMax;
      return cursoOk && periodoMinOk && periodoMaxOk;
    });
  }

  carregarEstudantes() {
    this.http.get<Estudante[]>('http://localhost:8080/student/list', { headers: this.getHeaders() })
      .subscribe({
        next: (dados) => this.estudantes = dados,
        error: (erro) => console.error('Erro ao carregar estudantes:', erro)
      });
  }

  async onSubmit(): Promise<void> {
    if (this.formEstudante.invalid) return;

    const formValue = this.formEstudante.value;

    const estudantePayload = {
      fullName: formValue.nomeCompleto,
      birthDate: formValue.dataNascimento,
      cpf: formValue.cpf,
      phone: formValue.telefone,
      course: formValue.curso,
      currentPeriod: formValue.periodoAtual,
      academicSummary: formValue.resumoAcademico,
      userId: this.authService.getUserId()
    };

    try {
      if (this.estudanteEditando) {
        await this.http.put(`http://localhost:8080/student/update/${this.estudanteEditando.id}`, estudantePayload, { headers: this.getHeaders() }).toPromise();
        alert('Estudante atualizado com sucesso!');
      } else {
        await this.http.post('http://localhost:8080/students', estudantePayload, { headers: this.getHeaders() }).toPromise();
        alert('Estudante cadastrado com sucesso!');
      }

      this.formEstudante.reset();
      this.mostrarFormulario = false;
      this.estudanteEditando = null;
      this.carregarEstudantes();
    } catch (error) {
      console.error('Erro ao salvar estudante:', error);
      alert('Erro ao salvar estudante!');
    }
  }

  editarEstudante(estudante: Estudante) {
    this.estudanteEditando = estudante;
    this.mostrarFormulario = true;

    this.formEstudante.setValue({
      nomeCompleto: estudante.nomeCompleto,
      dataNascimento: estudante.dataNascimento,
      cpf: estudante.cpf,
      telefone: estudante.telefone || '',
      curso: estudante.curso,
      periodoAtual: estudante.periodoAtual,
      resumoAcademico: estudante.resumoAcademico
    });
  }

  excluirEstudante(id: number) {
    if (!confirm('Deseja excluir este estudante?')) return;

    this.http.delete(`http://localhost:8080/student/delete/${id}`, { headers: this.getHeaders() })
      .subscribe({
        next: () => {
          alert('Estudante excluído com sucesso!');
          this.carregarEstudantes();
        },
        error: (erro) => {
          console.error('Erro ao excluir estudante:', erro);
          alert('Erro ao excluir estudante!');
        }
      });
  }

  cancelarCadastro() {
    this.formEstudante.reset();
    this.mostrarFormulario = false;
    this.estudanteEditando = null;
  }
}
