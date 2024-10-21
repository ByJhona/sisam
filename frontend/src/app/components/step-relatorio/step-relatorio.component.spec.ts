import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepRelatorioComponent } from './step-relatorio.component';

describe('StepRelatorioComponent', () => {
  let component: StepRelatorioComponent;
  let fixture: ComponentFixture<StepRelatorioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepRelatorioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepRelatorioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
