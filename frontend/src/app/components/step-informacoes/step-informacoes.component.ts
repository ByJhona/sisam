import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { DividaCalculada } from '../../types/dividaCalculada';
import { CardComponent } from '../card/card.component';
import { formatarMesAno } from '../../util/manipularData'

@Component({
  selector: 'app-step-informacoes',
  standalone: true,
  imports: [CardComponent],
  templateUrl: './step-informacoes.component.html',
  styleUrl: './step-informacoes.component.scss'
})
export class StepInformacoesComponent {
  @Input() dividaCalculada!: DividaCalculada;
  dataInicial: string = "E"
  dataFinal: string = "R"

  constructor(private cdref: ChangeDetectorRef) {}

  ngAfterViewInit() {
    console.log("teste")
    if (this.dividaCalculada != undefined) {
      this.dataInicial = formatarMesAno(this.dividaCalculada.dataInicial)
      this.dataFinal = formatarMesAno(this.dividaCalculada.dataFinal)
      this.cdref.detectChanges();
    }
  }

}
