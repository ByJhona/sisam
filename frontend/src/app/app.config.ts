import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {provideEnvironmentNgxMask} from 'ngx-mask'
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { Sucesso } from './interceptors/sucesso';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), 
  provideClientHydration(), provideAnimationsAsync(), 
  provideAnimationsAsync(), provideEnvironmentNgxMask(), 
  provideHttpClient(withFetch(), withInterceptors([])), { provide: HTTP_INTERCEPTORS, useClass: Sucesso, multi: true }]
};
