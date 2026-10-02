import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from '../../models/user';
import { AdminService } from '../../services/admin-service';
import { Message } from '../../models/message';
import { AdminNavBar } from '../admin-nav-bar/admin-nav-bar';

@Component({
  selector: 'app-admin-requests',
  standalone: true,
  imports: [CommonModule, AdminNavBar],
  templateUrl: './admin-requests.html',
  styleUrl: './admin-requests.css'
})
export class AdminRequests implements OnInit {
  private adminService = inject(AdminService);

  requests: User[] = [];
  message: Message | null = null;

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests() {
    this.adminService.getPendingRequests().subscribe({
      next: (data) => this.requests = data,
      error: (err) => console.error('Failed to fetch requests', err)
    });
  }

  approve(userId: number) {
    this.adminService.acceptRequest(userId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadRequests();
      },
      error: (err) => console.error('Failed to approve request', err)
    });
  }

  deny(userId: number) {
    this.adminService.denyRequest(userId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadRequests();
      },
      error: (err) => console.error('Failed to deny request', err)
    });
  }
}