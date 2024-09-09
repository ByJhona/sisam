import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Divida } from '../types/divida';

@Injectable({
  providedIn: 'root'
})
export class CalculadoraAPIService {
  private url = 'http://localhost:8080/'

  constructor(private http:HttpClient) { }

  enviarDados(dados : Divida):Observable<Divida>{
    return this.http.post<Divida>(this.url + 'calcular', dados)
  }

}
