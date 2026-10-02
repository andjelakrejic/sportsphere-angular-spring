import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-nav-bar',
  imports: [],
  templateUrl: './admin-nav-bar.html',
  styleUrl: './admin-nav-bar.css',
})
export class AdminNavBar {
  menuOpen = false;

  private router = inject(Router)

  openAccounts() {
    this.router.navigate(['/adminAccounts']);
  }

  openRequests() {
    this.router.navigate(['/adminRequests']);
  }

  openSports() {
    this.router.navigate(['/adminSports']);
  }

  openTrainers() {
    this.router.navigate(['/adminTrainers']);
  }

  openFacility() {
    this.router.navigate(['/adminFacility']);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/system-access']);
  }
}
