import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Sport } from '../../models/sport';
import { FacilityService } from '../../services/facility-service';
import { AdminService } from '../../services/admin-service';
import { Message } from '../../models/message';
import { AdminNavBar } from '../admin-nav-bar/admin-nav-bar';

@Component({
  selector: 'app-admin-sports',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminNavBar],
  templateUrl: './admin-sports.html',
  styleUrl: './admin-sports.css'
})
export class AdminSports implements OnInit {
  private facilityService = inject(FacilityService);
  private adminService = inject(AdminService);

  sports: Sport[] = [];
  newSportName = '';
  message: Message | null = null;

  ngOnInit(): void {
    this.loadSports();
  }

  loadSports() {
    this.facilityService.getAllSportsObject().subscribe({
      next: (data) => this.sports = data,
      error: (err) => console.error('Failed to fetch sports', err)
    });
  }

  addSport() {
    if (!this.newSportName.trim()) return;

    const newId = this.sports.length > 0
      ? Math.max(...this.sports.map(s => s.id)) + 1
      : 1;

    const sport: Sport = { id: newId, name: this.newSportName.trim() };

    this.adminService.addSport(sport).subscribe({
      next: (res) => {
        this.message = res;
        if (res.success) {
          this.newSportName = '';
          this.loadSports();
        }
      },
      error: (err) => console.error('Failed to add sport', err)
    });
  }
}