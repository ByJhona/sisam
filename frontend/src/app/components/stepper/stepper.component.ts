import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormBuilder, Validators, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import {MatDatepicker, MatDatepickerModule} from '@angular/material/datepicker';
import { MAT_DATE_FORMATS  } from '@angular/material/core';
import {MatMomentDateModule, provideMomentDateAdapter} from '@angular/material-moment-adapter';
import moment, { Moment } from 'moment';



// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};



@Component({
  selector: 'app-stepper',
  standalone: true,
  providers:[provideMomentDateAdapter(MY_FORMATS )],
  imports: [MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,MatFormFieldModule, MatInputModule, MatDatepickerModule, MatMomentDateModule],
      changeDetection: ChangeDetectionStrategy.OnPush,

  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss'
})
export class StepperComponent {
  private _formBuilder = inject(FormBuilder);

  infoDivida = this._formBuilder.group({
    valor: ['', Validators.required],
    dataInicial: ['', Validators.required],
    dataFinal: ['', Validators.required]
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });

  isLinear = false;

  setMonthAndYear(normalizedMonthAndYear: Moment, datepicker: MatDatepicker<Moment>) {
    const ctrlValue = this.infoDivida.get("dataInicial")?.value ?? moment();
    ctrlValue.month(normalizedMonthAndYear.month());
    ctrlValue.year(normalizedMonthAndYear.year());
    this.infoDivida.controls.dataInicial.setValue(ctrlValue);
    console.log(this.infoDivida)
    datepicker.close();
  }
}
