import { Component, Input, SimpleChange, SimpleChanges } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { Resultado } from '../../types/resultado';
import { Indice } from '../../types/Indice';
import { Observable } from 'rxjs';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He' },
  { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li' },
  { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be' },

];


@Component({
  selector: 'app-tabela',
  standalone: true,
  imports: [MatTableModule],
  templateUrl: './tabela.component.html',
  styleUrl: './tabela.component.scss'
})


export class TabelaComponent {
  @Input() resultApi!: Observable<Resultado>;
  @Input() indices!: Indice[];

  ngOnChanges(changes: SimpleChanges) {

    
    this.resultApi.subscribe(result => {
      this.indices = result.indices
      console.log("testeeeeeeee")
    })
  }
  ngOnInit() {

    console.log("diaxo")
  }


  displayedColumns: string[] = ['data', 'valor'];
  dataSource = this.indices;
}
