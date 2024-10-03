import { Component, inject, ViewChild } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { NgxMaskDirective } from 'ngx-mask';
import { Divida } from '../../types/divida';
import { CalculadoraAPIService } from '../../service/calculadora-api.service';
import { Indice } from '../../types/Indice';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { DividaCalculada } from '../../types/dividaCalculada';
import { meses } from '../../util/meses';
import { gerarAnos } from '../../util/anos';
import { CardComponent } from "../card/card.component";
import { formatarMesAno } from '../../util/manipularData';
import { MatIcon } from '@angular/material/icon';





@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule, MatFormFieldModule, MatInputModule, MatSelectModule, NgxMaskDirective, MatTableModule, MatPaginator, CardComponent, MatIcon],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {

  constructor(private calculadoraService: CalculadoraAPIService) {

  }
  // Criar logica para deixar o usuario acessar uma fase q ele nao tem acesso
  //Bug quando o usuario nao apertou o botao pra calcular e ele pode ir pra pagina 2
  documentoBinario: Blob = new Blob();
  isGerarRelatorio = false
  anos: number[] = gerarAnos()
  meses = meses
  dataInicial: string = ""
  dataFinal: string = ""
  displayedColumns: string[] = ['data', 'valor'];
  dataSource = new MatTableDataSource<Indice>();
  dividaCalculada: DividaCalculada = new DividaCalculada();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  private _formBuilder = inject(FormBuilder);
  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: ['', Validators.required],
    anoInicial: ['', Validators.required],
    mesFinal: ['', Validators.required],
    anoFinal: ['', [Validators.required]]
  }, { validators: this.dataFinalMenorDataFinal() });



  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  limparCampos(stepper: MatStepper): void {
    stepper.reset();
    this.infoDivida.get("valor")?.enable()
      this.infoDivida.get("mesInicial")?.enable()
      this.infoDivida.get("mesFinal")?.enable()
      this.infoDivida.get("anoInicial")?.enable()
      this.infoDivida.get("anoFinal")?.enable()
      this.isGerarRelatorio = false
    this.infoDivida.reset({ valor: '', mesInicial: '', anoFinal: '', mesFinal: '', anoInicial: '' })
  }

  onSubmit(stepper: MatStepper) {
    var isValido: boolean = this.validarDivida();

    if (isValido) {
      var divida: Divida = this.construirObjetoDivida();
      this.enviarApi(divida);
      stepper.next();
      this.infoDivida.get("valor")?.disable()
      this.infoDivida.get("mesInicial")?.disable()
      this.infoDivida.get("mesFinal")?.disable()
      this.infoDivida.get("anoInicial")?.disable()
      this.infoDivida.get("anoFinal")?.disable()

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

  ngOnInit(): void {
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
      this.dividaCalculada = new DividaCalculada(result.valorInicial, result.valorFinal, result.dataInicial, result.dataFinal, result.indices);


      this.dataInicial = formatarMesAno(this.dividaCalculada.dataInicial)
      this.dataFinal = formatarMesAno(this.dividaCalculada.dataFinal)

      console.log(this.dataInicial)
      console.log(this.dataFinal)
      console.log(this.dividaCalculada)

    })
  }

  gerarRelatorio(stepper: MatStepper) {
      this.isGerarRelatorio = true
    this.calculadoraService.baixarRelatorio(this.dividaCalculada.dataInicial, this.dividaCalculada.dataFinal).subscribe(result => {
      this.documentoBinario = new Blob([result], { type: 'application/pdf' });
      stepper.next();
      this.abrirRelatorio();

    });

  }

  baixarRelatorio() {
    const url = window.URL.createObjectURL(this.documentoBinario);
    const link = document.createElement('a')
    link.href = url
    link.download = 'Tabela de Atualização Monetária.pdf'; // Nome do arquivo para download
    link.click();
    window.URL.revokeObjectURL(url); // Limpa a URL criada
  }

  abrirRelatorio() {
    const url = window.URL.createObjectURL(this.documentoBinario);
    window.open(url);
    window.URL.revokeObjectURL(url); // Limpa a URL criada


  }
}
