import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Divida, formatarData } from '../types/divida';
import { Resultado } from '../types/resultado';

@Injectable({
  providedIn: 'root'
})
export class CalculadoraAPIService {
  private url = 'http://localhost:8080'

  constructor(private http:HttpClient) { }

  enviarDados(dados : Divida):Observable<Resultado>{
    var dataInicial = formatarData(dados.dataInicial);
    var dataFinal = formatarData(dados.dataFinal);
    return this.http.get<Resultado>(`${this.url}/calcular?valor=${dados.valor}&dataInicial=${dataInicial}&dataFinal=${dataFinal}`)
  }
}