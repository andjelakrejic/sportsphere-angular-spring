import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerReport } from './worker-report';

describe('WorkerReport', () => {
  let component: WorkerReport;
  let fixture: ComponentFixture<WorkerReport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerReport]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerReport);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
