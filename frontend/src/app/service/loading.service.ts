import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private _isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this._isLoading.asObservable(); 
  constructor() { }

  atualizarIsLoading( estado:boolean){
    this._isLoading.next(estado)
  }

  obterIsLoading():boolean{
    return this._isLoading.value;
  }
}
