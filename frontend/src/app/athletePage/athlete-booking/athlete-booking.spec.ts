import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteBooking } from './athlete-booking';

describe('AthleteBooking', () => {
  let component: AthleteBooking;
  let fixture: ComponentFixture<AthleteBooking>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteBooking]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteBooking);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
