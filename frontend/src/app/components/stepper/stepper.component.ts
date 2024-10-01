import { Component, inject, ViewChild } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { NgxMaskDirective } from 'ngx-mask';
import { TabelaComponent } from "../tabela/tabela.component";
import { Divida } from '../../types/divida';
import { CalculadoraAPIService } from '../../service/calculadora-api.service';
import { Resultado } from '../../types/resultado';
import { Indice } from '../../types/Indice';
import { Observable } from 'rxjs';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { format } from "date-fns"





@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule, MatFormFieldModule, MatInputModule, MatSelectModule, NgxMaskDirective, TabelaComponent, MatTableModule, MatPaginator],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {

  constructor(private calculadoraService: CalculadoraAPIService) {

  }

  private _formBuilder = inject(FormBuilder);
  meses = [
    { "nome": "Janeiro", "valor": 1 },
    { "nome": "Fevereiro", "valor": 2 },
    { "nome": "Março", "valor": 3 },
    { "nome": "Abril", "valor": 4 },
    { "nome": "Maio", "valor": 5 },
    { "nome": "Junho", "valor": 6 },
    { "nome": "Julho", "valor": 7 },
    { "nome": "Agosto", "valor": 8 },
    { "nome": "Setembro", "valor": 9 },
    { "nome": "Outubro", "valor": 10 },
    { "nome": "Novembro", "valor": 11 },
    { "nome": "Dezembro", "valor": 12 }
  ]
  anoInicial: number = 1994
  anos: number[] = []
  resultApi!: Observable<Resultado>
  valorTotal!: number
  indices!: Indice[]
  displayedColumns: string[] = ['data', 'valor'];
  dataSource = new MatTableDataSource<Indice>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }


  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: ['', Validators.required],
    anoInicial: ['', Validators.required],
    mesFinal: ['', Validators.required],
    anoFinal: ['', [Validators.required]]
  }, { validators: this.dataFinalMenorDataFinal() });

  limparCampos(): void {
    this.infoDivida.reset({ valor: '', mesInicial: '', anoFinal: '', mesFinal: '', anoInicial: '' })
  }

  onSubmit(stepper: MatStepper) {
    var isValido: boolean = this.validarDivida();

    if (isValido) {
      var divida: Divida = this.construirObjetoDivida();
      this.enviarApi(divida);
      stepper.next();
    }
  }

  dataFinalMenorDataFinal(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const mesInicial = control.get('mesInicial')?.value;
      const anoInicial = control.get('anoInicial')?.value;
      const mesFinal = control.get('mesFinal')?.value;
      const anoFinal = control.get('anoFinal')?.value;

      if (mesInicial && anoInicial && mesFinal && anoFinal) {
        const dataInicial = new Date(anoInicial, mesInicial - 1, 1); // Mês é baseado em 0 (janeiro = 0)
        const dataFinal = new Date(anoFinal, mesFinal - 1, 1);
        if (dataFinal < dataInicial) {
          control.get('anoFinal')?.setErrors({ dataFinalMenor: true })
          return { dataFinalMenor: true };
        }
      }
      control.get('anoFinal')?.updateValueAndValidity({ onlySelf: true })
      return null;
    }

  }

  gerarAnos(): number[] {
    const anoAtual = new Date().getFullYear();
    var anosGerados: number[] = []
    for (var i = this.anoInicial; i <= anoAtual; i++) {
      anosGerados.push(i);
    }
    return anosGerados.sort((a, b) => { return b - a });
  }

  ngOnInit(): void {
    this.anos = this.gerarAnos();
  }

  validarDivida(): boolean {
    if (this.infoDivida.valid) { // Se o formulario estiver válido...
      return true;
    } else {
      // Criar modal de alerta
      alert("Dados informados inválidos!")
      return false;
    }
  }

  construirObjetoDivida(): Divida {
    var valor = Number(this.infoDivida.get('valor')?.value);
    var mesInicial = Number(this.infoDivida.get('mesInicial')?.value);
    var anoInicial = Number(this.infoDivida.get('anoInicial')?.value);
    var mesFinal = Number(this.infoDivida.get('mesFinal')?.value);
    var anoFinal = Number(this.infoDivida.get('anoFinal')?.value);
    var dataInicial = new Date(anoInicial, mesInicial - 1);
    var dataFinal = new Date(anoFinal, mesFinal - 1)

    var divida: Divida = {
      valor: valor,
      dataInicial: dataInicial,
      dataFinal: dataFinal
    }

    return divida;
  }

  enviarApi(divida: Divida): void {
    this.calculadoraService.enviarDados(divida).subscribe((result) => {
      this.dataSource.data = result.indices;
      this.valorTotal = result.valor
    })
  }
}
