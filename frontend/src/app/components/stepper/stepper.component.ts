import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { NgxMaskDirective } from 'ngx-mask';
import { Divida } from '../../types/divida';
import enumTipo from '../../types/enumTipo';

import { CalculadoraAPIService } from '../../service/calculadora-api.service';
import { Indice } from '../../types/Indice';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatPaginator } from '@angular/material/paginator';
import { DividaCalculada } from '../../types/dividaCalculada';
import { mesesGerados } from '../../util/meses';
import { gerarAnos } from '../../util/anos';
import { CardComponent } from "../card/card.component";
import { formatarMesAno } from '../../util/manipularData';
import { MatIcon } from '@angular/material/icon';
import { LoadingService } from '../../service/loading.service';
import { MatTabsModule } from '@angular/material/tabs';
import { StepDadosComponent } from "../step-dados/step-dados.component";
import { StepInformacoesComponent } from "../step-informacoes/step-informacoes.component";
import { StepRelatorioComponent } from "../step-relatorio/step-relatorio.component";


@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule, MatStepperModule,
    FormsModule, ReactiveFormsModule,
    MatFormFieldModule, MatInputModule,
    MatSelectModule, NgxMaskDirective,
    CardComponent, MatIcon, MatProgressBarModule, MatTabsModule, StepDadosComponent, StepInformacoesComponent, StepRelatorioComponent],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {
  documentoBinario: Blob = new Blob();
  dividaCalculada!:DividaCalculada
  // Variaveis para controle das etapas
  etapas = {
    isEtapa1: false,
    isEtapa2: false,
    isEtapa3: false
  }
  //
  isLoading = false;
 
 

  constructor(private calculadoraService: CalculadoraAPIService, public loadingService: LoadingService) {

  }

  receberRespostaAPI(dividaCalculada:DividaCalculada){
    this.dividaCalculada = dividaCalculada;
    console.log(this.dividaCalculada)
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
