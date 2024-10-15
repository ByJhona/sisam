import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { LoadingService } from '../service/loading.service';

@Injectable()
export class Sucesso implements HttpInterceptor {

  constructor(private isLoading: LoadingService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    this.isLoading.atualizarIsLoading(true)

    return next.handle(request).pipe(
      
      finalize(() => {
        setInterval( () => {this.isLoading.atualizarIsLoading(false)}, 5000)
       
      })
    );
  }
}