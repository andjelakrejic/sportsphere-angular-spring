import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { RatingService } from '../../services/rating-service';

@Component({
  selector: 'app-athlete-reviews',
  standalone: true,
  imports: [CommonModule, AthleteNavBar],
  templateUrl: './athlete-reviews.html',
  styleUrl: './athlete-reviews.css',
})
export class AthleteReviews implements OnInit {
  comments: any[] = [];
  athleteId = 0;
  loading = true;
  errorMsg = '';

  private ratingService = inject(RatingService);

  ngOnInit(): void {
    const athlete = JSON.parse(localStorage.getItem('athlete')!);
    this.athleteId = athlete.id;

    this.ratingService.getMyComments(this.athleteId).subscribe({
      next: (data) => {
        this.comments = data;
        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Error loading your comments.';
        this.loading = false;
      }
    });
  }
}