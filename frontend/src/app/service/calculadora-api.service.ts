import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Divida } from '../types/divida';
import { formatarAnoMesDia } from '../util/manipularData';
import { DividaCalculada } from '../types/dividaCalculada';

@Injectable({
  providedIn: 'root'
})
export class CalculadoraAPIService {
  private url = 'http://localhost:8080'

  constructor(private http: HttpClient) { }

  enviarDados(dados: Divida): Observable<DividaCalculada> {
    var dataInicial = formatarAnoMesDia(dados.dataInicial);
    var dataFinal = formatarAnoMesDia(dados.dataFinal);
    return this.http.get<DividaCalculada>(`${this.url}/calcular?valor=${dados.valor}&dataInicial=${dataInicial}&dataFinal=${dataFinal}`)
  }
  baixarRelatorio(dataInicial: Date, dataFinal: Date): Observable<Blob> {
    return this.http.get<Blob>(`${this.url}/baixar-relatorio?dataInicial=${dataInicial}&dataFinal=${dataFinal}`,{responseType:'blob' as 'json'});
  }
}