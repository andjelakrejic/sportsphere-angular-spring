import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerNavBar } from './worker-nav-bar';

describe('WorkerNavBar', () => {
  let component: WorkerNavBar;
  let fixture: ComponentFixture<WorkerNavBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerNavBar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkerNavBar);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
