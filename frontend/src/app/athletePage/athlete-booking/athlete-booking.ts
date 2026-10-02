import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { Facility } from '../../models/facility';
import { FacilityService } from '../../services/facility-service';

@Component({
  selector: 'app-athlete-booking',
  standalone: true,
  imports: [CommonModule, FormsModule, AthleteNavBar],
  templateUrl: './athlete-booking.html',
  styleUrl: './athlete-booking.css'
})
export class AthleteBooking implements OnInit {
  cities: string[] = [];
  sports: string[] = [];
  facilities: Facility[] = [];
  message = '';

  searchParams = {
    name: '',
    city: '',
    sport: '',
    type: '',
    onlyFreeToday: false
  };

  private facilityService = inject(FacilityService);
  private router = inject(Router);

  ngOnInit() {
    this.facilityService.getActiveCities().subscribe(data => this.cities = data);
    this.facilityService.getAllSports().subscribe(data => this.sports = data);
  }

  search() {
    if (this.searchParams.onlyFreeToday) {
      this.facilityService.searchFreeTodayFacilites(
        this.searchParams.name,
        this.searchParams.city,
        this.searchParams.sport,
        this.searchParams.type
      ).subscribe({
        next: (data) => {
          this.facilities = data;
          this.message = data.length === 0 ? 'No facilities found.' : '';
        },
        error: (err) => {
          this.message = 'Error searching facilities.';
        }
      });
    } else {
      this.facilityService.searchFacilities(
        this.searchParams.name,
        this.searchParams.city,
        this.searchParams.sport,
        this.searchParams.type
      ).subscribe({
        next: (data) => {
          this.facilities = data;
          this.message = data.length === 0 ? 'No facilities found.' : '';
        },
        error: (err) => {
          this.message = 'Error searching facilities.';
        }
      });
    }
  }

  goToDetails(id: number){
    localStorage.setItem('facilityId', id.toString());
    this.router.navigate(['/athleteDetails']);
  }
}