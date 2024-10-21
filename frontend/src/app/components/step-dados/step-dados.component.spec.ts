import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StepDadosComponent } from './step-dados.component';

describe('StepDadosComponent', () => {
  let component: StepDadosComponent;
  let fixture: ComponentFixture<StepDadosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StepDadosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StepDadosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
