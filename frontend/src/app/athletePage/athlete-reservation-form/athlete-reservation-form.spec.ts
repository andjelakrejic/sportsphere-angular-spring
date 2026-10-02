import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteReservationForm } from './athlete-reservation-form';

describe('AthleteReservationForm', () => {
  let component: AthleteReservationForm;
  let fixture: ComponentFixture<AthleteReservationForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteReservationForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteReservationForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
