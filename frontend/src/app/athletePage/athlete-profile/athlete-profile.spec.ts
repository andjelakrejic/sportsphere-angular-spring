import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteProfile } from './athlete-profile';

describe('AthleteProfile', () => {
  let component: AthleteProfile;
  let fixture: ComponentFixture<AthleteProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteProfile);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
