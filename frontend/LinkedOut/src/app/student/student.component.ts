import { Component, OnInit, inject, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';
import { StudentService, Estudante } from './student.service'; // Importar o serviço

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule], // HttpClientModule removido
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private studentService = inject(StudentService); // Injetar o novo serviço
  public authService = inject(AuthService); // Público para uso no template
  private destroy$ = new Subject<void>();

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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
    this.studentService.getEstudantes()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (dados) => this.estudantes = dados,
        error: (erro) => console.error('Erro ao carregar estudantes:', erro)
      });
  }

  onSubmit(): void {
    if (this.formEstudante.invalid) return;

    const formValue = this.formEstudante.value;
    // O backend espera os campos com nomes diferentes, ajustamos aqui
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

    const action = this.estudanteEditando
      ? this.studentService.updateEstudante(this.estudanteEditando.id, estudantePayload)
      : this.studentService.createEstudante(estudantePayload);

    action.pipe(takeUntil(this.destroy$)).subscribe({
      next: () => {
        alert(`Estudante ${this.estudanteEditando ? 'atualizado' : 'cadastrado'} com sucesso!`);
        this.cancelarCadastro();
        this.carregarEstudantes();
      },
      error: (error) => {
        console.error('Erro ao salvar estudante:', error);
        alert('Erro ao salvar estudante!');
      }
    });
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

    this.studentService.deleteEstudante(id)
      .pipe(takeUntil(this.destroy$))
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
