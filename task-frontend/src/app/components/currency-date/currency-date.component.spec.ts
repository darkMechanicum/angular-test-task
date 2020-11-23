import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyDateComponent } from './currency-date.component';

describe('CurrencyDateComponent', () => {
  let component: CurrencyDateComponent;
  let fixture: ComponentFixture<CurrencyDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrencyDateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
