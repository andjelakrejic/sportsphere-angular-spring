import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerCalendar } from './worker-calendar';

describe('WorkerCalendar', () => {
  let component: WorkerCalendar;
  let fixture: ComponentFixture<WorkerCalendar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerCalendar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerCalendar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
