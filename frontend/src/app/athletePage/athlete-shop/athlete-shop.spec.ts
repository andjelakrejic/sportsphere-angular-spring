import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteEquipment } from './athlete-equipment';

describe('AthleteEquipment', () => {
  let component: AthleteEquipment;
  let fixture: ComponentFixture<AthleteEquipment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteEquipment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteEquipment);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
