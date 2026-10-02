import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerReservationTraining } from './worker-reservation-training';

describe('WorkerReservationTraining', () => {
  let component: WorkerReservationTraining;
  let fixture: ComponentFixture<WorkerReservationTraining>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerReservationTraining]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerReservationTraining);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
