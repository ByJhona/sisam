import { ChangeDetectorRef, Component, EventEmitter, Output, ViewChild } from '@angular/core';
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
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { DividaCalculada } from '../../types/dividaCalculada';
import { mesesGerados } from '../../util/meses';
import { gerarAnos } from '../../util/anos';
import { CardComponent } from "../card/card.component";
import { formatarMesAno } from '../../util/manipularData';
import { MatIcon } from '@angular/material/icon';
import { LoadingService } from '../../service/loading.service';
import { MatTabsModule } from '@angular/material/tabs';
import { BotaoComponent } from "../botao/botao.component";

@Component({
  selector: 'app-step-dados',
  standalone: true,
  imports: [MatButtonModule, MatStepperModule,
    FormsModule, ReactiveFormsModule,
    MatFormFieldModule, MatInputModule,
    MatSelectModule, NgxMaskDirective,
    CardComponent, MatIcon, MatProgressBarModule, MatTabsModule, BotaoComponent],
  templateUrl: './step-dados.component.html',
  styleUrl: './step-dados.component.scss'
})
export class StepDadosComponent {
  
  @Output() respostaApi: EventEmitter<DividaCalculada> = new EventEmitter<DividaCalculada>();

  anos: number[] = gerarAnos()
  meses = mesesGerados
  

  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: ['', Validators.required],
    anoInicial: ['', Validators.required],
    mesFinal: ['', Validators.required],
    anoFinal: ['', [Validators.required]]
  }, { validators: this.dataFinalMenorDataFinal() });

  constructor(private calculadoraService: CalculadoraAPIService, private _formBuilder: FormBuilder,) {

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

  toggleCampos(enable: boolean) {
    const fields = ['valor', 'mesInicial', 'anoInicial', 'mesFinal', 'anoFinal'];
    fields.forEach(field => enable ? this.infoDivida.get(field)?.enable() : this.infoDivida.get(field)?.disable());
  }


  onSubmit() {
    if (this.validarDivida()) {
      var divida: Divida = this.construirObjetoDivida();
      this.enviarApi(divida);
      this.toggleCampos(false);


    }
  }

  enviarApi(divida: Divida): void {
    this.calculadoraService.enviarDados(divida).subscribe((result) => {
      const dividaCalculada = new DividaCalculada(result.valorInicial, result.valorFinal, result.dataInicial, result.dataFinal, result.indices);
      this.respostaApi.emit(dividaCalculada);

    })
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

}
