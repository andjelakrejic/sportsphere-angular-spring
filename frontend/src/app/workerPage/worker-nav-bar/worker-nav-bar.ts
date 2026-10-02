import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-worker-nav-bar',
  imports: [],
  templateUrl: './worker-nav-bar.html',
  styleUrl: './worker-nav-bar.css',
})
export class WorkerNavBar {
   menuOpen = false;

  private router = inject(Router)

  openProfile() {
    this.router.navigate(['/workerProfile']);
  }

  openFacilities() {
    this.router.navigate(['/workerFacilities']);
  }

  openReservationTraining() {
    this.router.navigate(['/workerReservationTraining']);
  }

  openPromotionsEquipment() {
    this.router.navigate(['/workerPromotionEquipment']);
  }

  openCalendar() {
    this.router.navigate(['/workerCalendar'])
  }

  openReport() {
    this.router.navigate(['/workerReport'])
  }

  logout() {
    localStorage.removeItem('worker');
    this.router.navigate(['/']);
  }
}
