import { Component, inject } from '@angular/core';
import { FacilityService } from '../services/facility-service';
import { Facility } from '../models/facility';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  imports: [FormsModule, CommonModule],
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {
  cities: string[] = [];
  sports: string[] = [];
  facilities: Facility[] = [];
  message1: string = ""
  message2: string = ""

  private facilityService = inject(FacilityService)
  private router = inject(Router)

  sortAsc = true;
  sortCol = '';

  searchParams = {
    name: '',
    city: '',
    sport: '',
    type: ''
  }

  ngOnInit() {
    this.facilityService.getActiveCities().subscribe(data => {
      if(data == null){
        this.message1 = "No cities are active"
      } else {
        this.message1 = ""
        this.cities = data
      }
    });
    
    this.facilityService.getAllSports().subscribe((data => {
      if(data == null){
        this.message2 = "Error getting sports"
      } else {
        this.message2 = ""
        this.sports = data
      }
    }));
  }

  search() {
    this.facilityService.searchFacilities(
      this.searchParams.name,
      this.searchParams.city,
      this.searchParams.sport,
      this.searchParams.type
    ).subscribe(data => this.facilities = data);
  }

  sort(col: string) {
    if (this.sortCol === col) {
      this.sortAsc = !this.sortAsc;
    } else {
      this.sortCol = col;
      this.sortAsc = true;
    }

    this.facilities = [...this.facilities].sort((a: any, b: any) => {
      let valA = a[col] ?? '';
      let valB = b[col] ?? '';

      if (col === 'sports') {
        valA = this.firstSportAlphabetically(valA);
        valB = this.firstSportAlphabetically(valB);
      }

      return this.sortAsc
        ? valA.localeCompare(valB)
        : valB.localeCompare(valA);
    });
  }

  private firstSportAlphabetically(sportsString: string): string {
    if (!sportsString) return '';
    return sportsString
      .split(',')
      .map(s => s.trim())
      .sort((a, b) => a.localeCompare(b))[0];
  }

  goToDetails(id: number){
    localStorage.setItem("facilityId", JSON.stringify(id))
    this.router.navigate(["details"])
  }
}
