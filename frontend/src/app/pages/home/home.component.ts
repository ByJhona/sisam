import { Component } from '@angular/core';
import { StepperComponent } from "../../components/stepper/stepper.component";
import { CabecalhoComponent } from "../../components/cabecalho/cabecalho.component";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ StepperComponent, CabecalhoComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
