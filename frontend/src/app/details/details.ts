import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FacilityService } from '../services/facility-service';
import { Facility } from '../models/facility';
import { CourtService } from '../services/court-service';
import { Court } from '../models/court';
import { Header } from '../header/header';
import { RatingService } from '../services/rating-service';

@Component({
  selector: 'app-details',
  imports: [Header],
  templateUrl: './details.html',
  styleUrl: './details.css',
})
export class Details implements OnInit{

  facility: Facility = new Facility()
  currentImage = 0
  images: string[] = []
  courts: Court[] = []
  ratingSummary = {likes: 0, dislikes: 0}

  private router = inject(Router)
  private facilityService = inject(FacilityService)
  private courtService = inject(CourtService)
  private ratingService = inject(RatingService)
  
  
  ngOnInit(): void {
    let facilityId = Number(localStorage.getItem("facilityId"))
    
    this.facilityService.getFacility(facilityId).subscribe(data => {
      if (data == null){
        alert("Error getting facility")
      } else {
        this.facility = data
      }

      this.ratingService.getSummary(this.facility.id).subscribe({
      next: (data) => this.ratingSummary = data,
      error: (err) => console.error(err)
    });
    })

    this.facilityService.getFacilityImages(facilityId).subscribe({
      next: (data) => {
        this.images = data.map(filename => `http://localhost:8080/images/${filename}`);
        this.preloadImages();
      }
    });

    this.courtService.getAvailableCourts(facilityId).subscribe(data => { // INDOOR OUTDOR??
      if (data == null){
        alert("Error getting available courts")
      } else {
        this.courts = data
      }
    })
  }

  preloadImages() {
    this.images.forEach(url => {
      const img = new Image();
      img.src = url;
    });
  }
  
  prevImage() {
        this.currentImage = this.currentImage === 0 
            ? this.images.length - 1 
            : this.currentImage - 1
    }

    nextImage() {
        this.currentImage = this.currentImage === this.images.length - 1 
            ? 0 
            : this.currentImage + 1
    }

}
