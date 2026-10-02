import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-athlete-nav-bar',
  standalone: true,
  imports: [],
  templateUrl: './athlete-nav-bar.html',
  styleUrl: './athlete-nav-bar.css',
})
export class AthleteNavBar {
  menuOpen = false;

  private router = inject(Router)

  openProfile() {
    this.router.navigate(['/athleteProfile']);
  }

  openBooking() {
    this.router.navigate(['/athleteBooking']);
  }

  openTraining() {
    this.router.navigate(['/athleteTraining']);
  }

  openSparing() {
    this.router.navigate(['/athleteSparing']);
  }

  openEquipment() {
    this.router.navigate(['/athleteShop']);
  }

  openReviews() {
    this.router.navigate(['/athleteReviews']);
  }

  openStatistics() {
    this.router.navigate(['/athleteStatistics']);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}