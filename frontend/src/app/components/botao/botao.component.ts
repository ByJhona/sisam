import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-botao',
  standalone: true,
  imports: [],
  templateUrl: './botao.component.html',
  styleUrl: './botao.component.scss'
})
export class BotaoComponent {
  @Input() label!: string;
  @Input() estilo!: string;
  @Input() tipo:string = 'button'
  @Output() acionar: EventEmitter<boolean> = new EventEmitter<boolean>()

  clicou(){
    this.acionar.emit(true)
  }

}
