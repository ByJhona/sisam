import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
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
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { MatPaginator } from '@angular/material/paginator';
import { DividaCalculada } from '../../types/dividaCalculada';
import { meses } from '../../util/meses';
import { gerarAnos } from '../../util/anos';
import { CardComponent } from "../card/card.component";
import { formatarMesAno } from '../../util/manipularData';
import { MatIcon } from '@angular/material/icon';
import { LoadingService } from '../../service/loading.service';

@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule, MatStepperModule,
    FormsModule, ReactiveFormsModule,
    MatFormFieldModule, MatInputModule,
    MatSelectModule, NgxMaskDirective,
    MatTableModule, MatPaginator,
    CardComponent, MatIcon, MatProgressBarModule],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {
  documentoBinario: Blob = new Blob();
  // Variaveis para controle das etapas
  etapas = {
    isEtapa1: false,
    isEtapa2: false,
    isEtapa3: false
  }
  //
isLoading = false;
  anos: number[] = gerarAnos()
  meses = meses
  dataInicial: string = ""
  dataFinal: string = ""
  displayedColumns: string[] = ['data', 'valor'];
  dataSource = new MatTableDataSource<Indice>();
  dividaCalculada: DividaCalculada = new DividaCalculada();
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  
  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: ['', Validators.required],
    anoInicial: ['', Validators.required],
    mesFinal: ['', Validators.required],
    anoFinal: ['', [Validators.required]]
  }, { validators: this.dataFinalMenorDataFinal() });

  constructor(private calculadoraService: CalculadoraAPIService, private _formBuilder: FormBuilder,private cd: ChangeDetectorRef, public loadingService: LoadingService) {

  }
  ngOnInit(){
    this.loadingService.isLoading$.subscribe(estado => {
      this.isLoading = estado;
    })
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
  toggleCampos(enable: boolean) {
    const fields = ['valor', 'mesInicial', 'anoInicial', 'mesFinal', 'anoFinal'];
    fields.forEach(field => enable ? this.infoDivida.get(field)?.enable() : this.infoDivida.get(field)?.disable());
  }

  limparCampos(stepper: MatStepper): void {
    stepper.reset();
    this.toggleCampos(true);
    this.etapas.isEtapa1 = false;
    this.etapas.isEtapa2 = false;
    this.etapas.isEtapa3 = false;
    this.infoDivida.reset();
  }

  onSubmit(stepper: MatStepper) {
    if (this.validarDivida()) {
      var divida: Divida = this.construirObjetoDivida();
      this.enviarApi(divida, stepper);
      this.toggleCampos(false);
      

    }
  }

  dataFinalMenorDataFinal(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const { mesInicial, anoInicial, mesFinal, anoFinal } = control.value;

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

  enviarApi(divida: Divida, stepper:MatStepper): void {
    this.calculadoraService.enviarDados(divida).subscribe((result) => {
      this.dataSource.data = result.indices;
      console.log(this.dataSource.data)
      this.dividaCalculada = new DividaCalculada(result.valorInicial, result.valorFinal, result.dataInicial, result.dataFinal, result.indices);

      this.dataInicial = formatarMesAno(this.dividaCalculada.dataInicial)
      this.dataFinal = formatarMesAno(this.dividaCalculada.dataFinal)
      this.etapas.isEtapa1 = true
      this.etapas.isEtapa2 = true
      this.cd.detectChanges();
      stepper.next();  // Avançar para a próxima etapa
    })
  }

  gerarRelatorio(stepper: MatStepper) {
    this.etapas.isEtapa3 = true
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
