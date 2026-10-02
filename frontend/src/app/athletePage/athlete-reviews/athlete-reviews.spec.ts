import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteReviews } from './athlete-reviews';

describe('AthleteReviews', () => {
  let component: AthleteReviews;
  let fixture: ComponentFixture<AthleteReviews>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteReviews]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteReviews);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
