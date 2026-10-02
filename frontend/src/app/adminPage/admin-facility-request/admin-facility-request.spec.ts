import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFacilityRequest } from './admin-facility-request';

describe('AdminFacilityRequest', () => {
  let component: AdminFacilityRequest;
  let fixture: ComponentFixture<AdminFacilityRequest>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFacilityRequest]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFacilityRequest);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
