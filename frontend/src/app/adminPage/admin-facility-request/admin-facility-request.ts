import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Facility } from '../../models/facility';
import { AdminService } from '../../services/admin-service';
import { Message } from '../../models/message';
import { AdminNavBar } from '../admin-nav-bar/admin-nav-bar';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-facility-request',
  standalone: true,
  imports: [CommonModule, AdminNavBar, FormsModule],
  templateUrl: './admin-facility-request.html',
  styleUrl: './admin-facility-request.css'
})
export class AdminFacilityRequest implements OnInit {
  private adminService = inject(AdminService);

  facilities: Facility[] = [];
  message: Message | null = null;

  ngOnInit(): void {
    this.loadFacilities();
  }

  loadFacilities() {
    this.adminService.getPendingFacilities().subscribe({
      next: (data) => this.facilities = data,
      error: (err) => console.error('Failed to fetch pending facilities', err)
    });
  }

  approve(facilityId: number) {
    this.adminService.acceptFacilityRequest(facilityId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadFacilities();
      },
      error: (err) => console.error('Failed to approve facility', err)
    });
  }

  deny(facilityId: number) {
    this.adminService.denyFacilityRequest(facilityId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadFacilities();
      },
      error: (err) => console.error('Failed to deny facility', err)
    });
  }
}