import {  Component, inject } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import { NgxMaskDirective } from 'ngx-mask';






@Component({
  selector: 'app-stepper',
  standalone: true,
  imports: [MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,MatFormFieldModule, MatInputModule, MatSelectModule, NgxMaskDirective],

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {

  
  private _formBuilder = inject(FormBuilder);
  meses = [
  {"nome": "Janeiro", "valor": 1},
  {"nome": "Fevereiro", "valor": 2},
  {"nome": "Março", "valor": 3},
  {"nome": "Abril", "valor": 4},
  {"nome": "Maio", "valor": 5},
  {"nome": "Junho", "valor": 6},
  {"nome": "Julho", "valor": 7},
  {"nome": "Agosto", "valor": 8},
  {"nome": "Setembro", "valor": 9},
  {"nome": "Outubro", "valor": 10},
  {"nome": "Novembro", "valor": 11},
  {"nome": "Dezembro", "valor": 12}
]
  anos: number[] = []

  gerarAnos(): number[]{
    var dataAtual = new Date();
    var anoAtual = dataAtual.getFullYear();
    var anosGerados: number[] = []
    for(var i=1994;i<= anoAtual; i++){
      anosGerados.push(i);
    }
    return anosGerados;
  }


  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    mesInicial: ['', Validators.required],
    anoInicial: ['', Validators.required],
    mesFinal: ['', Validators.required],
    anoFinal: ['', Validators.required]
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });

  isLinear = false;

  
  ngOnInit():void{
    this.anos = this.gerarAnos();
  }
}
