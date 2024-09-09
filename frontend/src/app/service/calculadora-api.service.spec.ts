import { TestBed } from '@angular/core/testing';

import { CalculadoraAPIService } from './calculadora-api.service';

describe('CalculadoraAPIService', () => {
  let service: CalculadoraAPIService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CalculadoraAPIService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
