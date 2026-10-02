import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteDetails } from './athlete-details';

describe('AthleteDetails', () => {
  let component: AthleteDetails;
  let fixture: ComponentFixture<AthleteDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
