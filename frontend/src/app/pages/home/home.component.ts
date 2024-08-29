import { Component } from '@angular/core';
import { FormularioComponent } from "../../components/formulario/formulario.component";
import { StepperComponent } from "../../components/stepper/stepper.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormularioComponent, StepperComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
