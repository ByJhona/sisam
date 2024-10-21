import { Component, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Indice } from '../../types/Indice';


@Component({
  selector: 'app-tabela',
  standalone: true,
  imports: [MatPaginator, MatTableModule],
  templateUrl: './tabela.component.html',
  styleUrl: './tabela.component.scss'
})
export class TabelaComponent {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @Input() indices!: Indice[];
  displayedColumns: string[] = ['data', 'valor'];
  dataSource = new MatTableDataSource<Indice>();

  ngAfterViewInit() {
    this.dataSource.data = this.indices;
    this.dataSource.paginator = this.paginator;
  }

}
