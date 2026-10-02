import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerPromotionsEquipment } from './worker-promotions-equipment';

describe('WorkerPromotionsEquipment', () => {
  let component: WorkerPromotionsEquipment;
  let fixture: ComponentFixture<WorkerPromotionsEquipment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerPromotionsEquipment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerPromotionsEquipment);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
