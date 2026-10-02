import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteStatistics } from './athlete-statistics';

describe('AthleteStatistics', () => {
  let component: AthleteStatistics;
  let fixture: ComponentFixture<AthleteStatistics>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteStatistics]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteStatistics);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
