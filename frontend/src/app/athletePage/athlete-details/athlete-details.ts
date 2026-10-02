import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AthleteCalendar } from '../athlete-calendar/athlete-calendar';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { Facility } from '../../models/facility';
import { Court } from '../../models/court';
import { FacilityService } from '../../services/facility-service';
import { CourtService } from '../../services/court-service';
import { RatingService } from '../../services/rating-service';
import { AthleteReservationForm } from '../athlete-reservation-form/athlete-reservation-form';
import { AthleteBlockStatus } from '../../models/athleteBlockStatus';
import { WorkerReservationService } from '../../services/worker-reservation-service';

@Component({
  selector: 'app-athlete-details',
  standalone: true,
  imports: [CommonModule, FormsModule, AthleteCalendar, AthleteNavBar, AthleteReservationForm],
  templateUrl: './athlete-details.html',
  styleUrl: './athlete-details.css'
})
export class AthleteDetails implements OnInit {
  facility: Facility = new Facility();
  courts: Court[] = [];
  images: string[] = [];
  currentImage = 0;
  athleteId = 0;
  facilityId = 0;
  loading = true;
  errorMsg = '';

  blockStatus: AthleteBlockStatus | null = null;

  // rating
  comments: any[] = [];
  ratingSummary = { likes: 0, dislikes: 0 };
  newComment = '';
  ratingErrorMsg = '';

  private facilityService = inject(FacilityService);
  private courtService = inject(CourtService);
  private ratingService = inject(RatingService);
  private reservationService = inject(WorkerReservationService)

  private router = inject(Router);

  ngOnInit(): void {
    const facilityId = Number(localStorage.getItem('facilityId'));
    const athlete = JSON.parse(localStorage.getItem('athlete')!);
    this.athleteId = athlete.id;
    this.facilityId = facilityId;

    if (!facilityId) {
      this.errorMsg = 'No facility selected.';
      this.loading = false;
      return;
    }

    this.facilityService.getFacility(facilityId).subscribe({
      next: (data) => this.facility = data,
      error: () => this.errorMsg = 'Error loading facility.'
    })

    this.facilityService.getFacilityImages(facilityId).subscribe({
      next: (data) => {
        this.images = data.map(filename => `http://localhost:8080/images/${filename}`);
        this.preloadImages();
      }
    });

    this.courtService.getCourtsForFacility(facilityId).subscribe({
      next: (data) => {
        this.courts = data;
        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Error loading courts.';
        this.loading = false;
      }
    })

    this.loadRatingData()
    this.checkBlockStatus()
  }

  prevImage() {
    this.currentImage = this.currentImage === 0 ? this.images.length - 1 : this.currentImage - 1
  }

  preloadImages() {
    this.images.forEach(url => {
      const img = new Image();
      img.src = url;
    });
  }
  
  nextImage() {
    this.currentImage = this.currentImage === this.images.length - 1 ? 0 : this.currentImage + 1
  }

  myReaction: string | null = null

  loadRatingData(): void {
    this.ratingService.getLast5Comments(this.facilityId, this.athleteId).subscribe({
      next: (data) => this.comments = data,
      error: (err) => console.error(err)
    })

    this.ratingService.getSummary(this.facilityId).subscribe({
      next: (data) => this.ratingSummary = data,
      error: (err) => console.error(err)
    })

    this.ratingService.getMyReaction(this.athleteId, this.facilityId).subscribe({
      next: (data) => this.myReaction = data || null,
      error: (err) => console.error(err)
    })
  }

  react(type: string): void {
    this.ratingErrorMsg = ''
    this.ratingService.addReaction(this.athleteId, this.facilityId, type).subscribe({
      next: (res: any) => {
        if (res.success) {
          this.loadRatingData()
        } else {
          this.ratingErrorMsg = res.message
        }
      },
      error: (err) => console.error(err)
    })
  }

  submitComment(): void {
    if (!this.newComment.trim()) return
    this.ratingErrorMsg = ''

    this.ratingService.addComment(this.athleteId, this.facilityId, this.newComment).subscribe({
      next: (res: any) => {
        if (res.success) {
          this.newComment = ''
          this.loadRatingData()
          alert("Comment added!")
        } else {
          this.ratingErrorMsg = res.message
        }
      },
      error: (err) => console.error(err)
    })
  }

  checkBlockStatus() {
    this.reservationService.getBlockStatus(this.athleteId, this.facilityId).subscribe({
      next: (status) => this.blockStatus = status,
      error: () => this.blockStatus = null
    })
  }

  loadFacilityImages() {
    this.facilityService.getFacilityImages(this.facilityId).subscribe(data => {
      this.images = data.map(filename => `http://localhost:8080/images/${filename}`)
    })
  }
}