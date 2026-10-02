import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteNavBar } from './athlete-nav-bar';

describe('AthleteNavBar', () => {
  let component: AthleteNavBar;
  let fixture: ComponentFixture<AthleteNavBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteNavBar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteNavBar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
