import { Component, EventEmitter, Input, Output, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IndividualTrainingService } from '../services/individual-training-service';
import { Trainer } from '../models/trainer';


@Component({
  selector: 'app-booking-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './booking-form.html',
  styleUrl: './booking-form.css'
})
export class BookingForm implements OnInit {
  private trainingService = inject(IndividualTrainingService);

  @Input({ required: true }) trainer!: Trainer;
  @Input({ required: true }) facilityId!: number;
  @Input({ required: true }) sportId!: number;
  @Output() close = new EventEmitter<void>();

  date: string = '';
  time: string = '';
  timeSlots: string[] = [];
  errorMessage: string = '';
  successMessage: string = '';

  ngOnInit() {
    this.generateTimeSlots();
  }

  generateTimeSlots() {
    this.timeSlots = [];
    for (let h = 0; h < 24; h++) {
      this.timeSlots.push(`${h.toString().padStart(2, '0')}:00`);
    }
  }

  submit() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.date || !this.time) {
      this.errorMessage = 'Please select a date and time.';
      return;
    }

    const athlete = JSON.parse(localStorage.getItem('athlete') || '{}');

    const startHour = parseInt(this.time.split(':')[0]);
    const endTime = `${(startHour + 1).toString().padStart(2, '0')}:00:00`;

    const training = {
      athleteId: athlete.id,
      trainerId: this.trainer.id,
      facilityId: this.facilityId,
      sportId: this.sportId,
      date: this.date,
      startTime: `${this.time}:00`,
      endTime: endTime
    };

    this.trainingService.bookTraining(training).subscribe({
      next: (res: any) => {
        if (res.success) {
          this.successMessage = res.message || 'Training booked successfully!';
        } else {
          this.errorMessage = res.message || 'Booking failed. Please try again.';
        }
      },
      error: () => {
        this.errorMessage = 'Booking failed. Please try again.';
      }
    });
  }

  onClose() {
    this.close.emit();
  }
}