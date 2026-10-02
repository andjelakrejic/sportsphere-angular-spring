import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteTraining } from './athlete-training';

describe('AthleteTraining', () => {
  let component: AthleteTraining;
  let fixture: ComponentFixture<AthleteTraining>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteTraining]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteTraining);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
