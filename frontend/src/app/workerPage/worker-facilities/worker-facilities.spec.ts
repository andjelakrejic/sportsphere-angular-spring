import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerFacilities } from './worker-facilities';

describe('WorkerFacilities', () => {
  let component: WorkerFacilities;
  let fixture: ComponentFixture<WorkerFacilities>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerFacilities]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerFacilities);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
