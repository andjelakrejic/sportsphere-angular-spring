import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTrainers } from './admin-trainers';

describe('AdminTrainers', () => {
  let component: AdminTrainers;
  let fixture: ComponentFixture<AdminTrainers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTrainers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTrainers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
