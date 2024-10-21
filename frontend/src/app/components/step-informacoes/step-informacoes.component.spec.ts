import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepInformacoesComponent } from './step-informacoes.component';

describe('StepInformacoesComponent', () => {
  let component: StepInformacoesComponent;
  let fixture: ComponentFixture<StepInformacoesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepInformacoesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepInformacoesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
