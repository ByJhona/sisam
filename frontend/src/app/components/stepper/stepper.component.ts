import { Component, inject } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepper, MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { NgxMaskDirective } from 'ngx-mask';






@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule, MatFormFieldModule, MatInputModule, MatSelectModule, NgxMaskDirective],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {

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

  gerarAnos(): number[] {
    var dataAtual = new Date();
    var anoAtual = dataAtual.getFullYear();
    var anosGerados: number[] = []
    for (var i = this.anoInicial; i <= anoAtual; i++) {
      anosGerados.push(i);
    }
    return anosGerados;
  }


  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: [null, Validators.required],
    anoInicial: [null, Validators.required],
    mesFinal: [null, Validators.required],
    anoFinal: [null, Validators.required]
  }, { validators: this.dataFinalMenorDataFinal() });


  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });


  limparCampos(): void {
    this.infoDivida.reset({ valor: '', mesInicial: null, anoFinal: null, mesFinal: null, anoInicial: null })
  }

  onSubmit(stepper: MatStepper) {
    this.infoDivida.updateValueAndValidity(); // Atualiza o estado de validação

    console.log(this.infoDivida.errors?.['dataFinalMenor'])
    stepper.next()



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
      return null;
    }

  }


  ngOnInit(): void {
    this.anos = this.gerarAnos();
  }
}
