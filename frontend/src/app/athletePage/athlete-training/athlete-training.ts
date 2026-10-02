import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Trainer } from '../../models/trainer';
import { Facility } from '../../models/facility';
import { Sport } from '../../models/sport';
import { TrainerService } from '../../services/trainer-service';
import { FacilityService } from '../../services/facility-service';
import { BookingForm } from '../../booking-form/booking-form';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';

@Component({
  selector: 'app-athlete-training',
  standalone: true,
  imports: [FormsModule, AthleteNavBar, BookingForm],
  templateUrl: './athlete-training.html',
  styleUrl: './athlete-training.css'
})
export class AthleteTraining {
  private trainerService = inject(TrainerService);
  private facilityService = inject(FacilityService);

  facilities: Facility[] = [];
  sports: Sport[] = [];
  trainers: Trainer[] = [];

  selectedFacilityId: number | null = null;
  selectedSportId: number | null = null;
  selectedTrainer: Trainer | null = null;

  ngOnInit() {
    this.facilityService.getActiveFacilities().subscribe(data => this.facilities = data);
    this.facilityService.getAllSportsObject().subscribe(data => this.sports = data);
  }

  onSelectionChange() {
    this.trainers = [];
    if (this.selectedFacilityId && this.selectedSportId) {
      this.trainerService
        .getTrainersByFacilityAndSport(this.selectedFacilityId, this.selectedSportId)
        .subscribe(data => this.trainers = data);
    }
  }

  openBookingForm(trainer: Trainer) {
    this.selectedTrainer = trainer;
  }

  closeBookingForm() {
    this.selectedTrainer = null;
  }

  getTrainerImageUrl(trainer: Trainer): string {
    if (!trainer.image) return 'assets/default-avatar.png';
    return `http://localhost:8080/images/${trainer.image}`;
  }
}