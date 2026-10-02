import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteCalendar } from './athlete-calendar';

describe('AthleteCalendar', () => {
  let component: AthleteCalendar;
  let fixture: ComponentFixture<AthleteCalendar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteCalendar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteCalendar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
