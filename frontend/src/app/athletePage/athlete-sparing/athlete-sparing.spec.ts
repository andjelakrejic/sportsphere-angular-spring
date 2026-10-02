import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteSparing } from './athlete-sparing';

describe('AthleteSparing', () => {
  let component: AthleteSparing;
  let fixture: ComponentFixture<AthleteSparing>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteSparing]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteSparing);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
