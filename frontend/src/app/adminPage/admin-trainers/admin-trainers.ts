import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Trainer } from '../../models/trainer';
import { TrainerService } from '../../services/trainer-service';
import { AdminService } from '../../services/admin-service';
import { Message } from '../../models/message';
import { AdminNavBar } from '../admin-nav-bar/admin-nav-bar';

@Component({
  selector: 'app-admin-trainers',
  standalone: true,
  imports: [CommonModule, AdminNavBar],
  templateUrl: './admin-trainers.html',
  styleUrl: './admin-trainers.css'
})
export class AdminTrainers implements OnInit {
  private trainerService = inject(TrainerService);
  private adminService = inject(AdminService);

  trainers: Trainer[] = [];
  message: Message | null = null;

  ngOnInit(): void {
    this.loadTrainers();
  }

  loadTrainers() {
    this.trainerService.getAllTrainers().subscribe({
      next: (data) => this.trainers = data,
      error: (err) => console.error('Failed to fetch trainers', err)
    });
  }

  deactivate(trainerId: number) {
    if (!confirm('Are you sure you want to deactivate this trainer?')) return;

    this.adminService.deactivateTrainer(trainerId).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) this.loadTrainers();
      },
      error: (err) => console.error('Failed to deactivate trainer', err)
    });
  }
}