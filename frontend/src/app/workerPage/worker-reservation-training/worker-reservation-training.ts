import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { Facility } from '../../models/facility';
import { WorkerReservationDTO } from '../../models/workerResevationDTO';
import { WorkerTrainingDTO } from '../../models/workerTrainingDTO';
import { FacilityService } from '../../services/facility-service';
import { WorkerReservationService } from '../../services/worker-reservation-service';
import { FormsModule } from '@angular/forms';
import { SlicePipe } from '@angular/common';

@Component({
  selector: 'app-worker-reservation-training',
  imports: [WorkerNavBar, FormsModule, SlicePipe],
  templateUrl: './worker-reservation-training.html',
  styleUrl: './worker-reservation-training.css',
})
export class WorkerReservationTraining implements OnInit, OnDestroy{
  workerId: number = 0;
  facilities: Facility[] = [];
  selectedFacilityId: number | null = null;

  reservations: WorkerReservationDTO[] = [];
  trainings: WorkerTrainingDTO[] = [];

  feedbackMessage: string = "";
  feedbackSuccess: boolean = false;

  private tickInterval: any;
  now: Date = new Date();

  private facilityService = inject(FacilityService)
  private reservationService = inject(WorkerReservationService)

  ngOnInit(): void {
    const worker = JSON.parse(localStorage.getItem('worker') || 'null');
    this.workerId = worker ? worker.id : 0;
    this.loadFacilities();

    // osvezavaj "sada" svakih 30s da dugmad Potvrdi/Odjavi nestanu tacno kad istekne prozor od 10 min
    this.tickInterval = setInterval(() => {
      this.now = new Date();
    }, 30000);
  }

  ngOnDestroy(): void {
    if (this.tickInterval) {
      clearInterval(this.tickInterval);
    }
  }

  loadFacilities(): void {
    if (!this.workerId) return;

    this.facilityService.getFacilitiesByWorker(this.workerId).subscribe({
      next: (data) => {
        this.facilities = data;
        if (this.facilities.length > 0 && this.selectedFacilityId === null) {
          this.selectedFacilityId = this.facilities[0].id;
          this.loadData();
        }
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load facilities.";
      }
    });
  }

  onFacilityChange(): void {
    this.loadData();
  }

  loadData(): void {
    if (this.selectedFacilityId === null) return;

    this.reservationService.getReservations(this.selectedFacilityId).subscribe({
      next: (data) => this.reservations = data,
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load reservations.";
      }
    });

    this.reservationService.getTrainings(this.selectedFacilityId).subscribe({
      next: (data) => this.trainings = data,
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load trainings.";
      }
    });
  }

  // da li je termin poceo I da li je jos u prozoru od 10 minuta
  isWithinActionWindow(start: Date): boolean {
    const diffMinutes = (this.now.getTime() - start.getTime()) / 60000;
    return diffMinutes >= 0 && diffMinutes <= 10;
  }


  confirmReservation(r: WorkerReservationDTO): void {
    this.reservationService.confirmReservation(r.id).subscribe({  
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) this.loadData();
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while confirming reservation.";
      }
    });
  }
  
  formatStatus(status: string): string {
  switch (status) {
    case 'BOOKED':
      return 'Upcoming';
    case 'CONFIRMED': return 'Confirmed';
    case 'NO_SHOW': return 'Missed';
    case 'CANCELLED': return 'Cancelled';
    default: return status;
  }
}

showReservationActions(r: WorkerReservationDTO): boolean {
  if (r.status !== 'BOOKED') return false;
  const start = new Date(`${r.date}T${r.timeFrom}`);
  return this.isWithinActionWindow(start);
}

showTrainingActions(t: WorkerTrainingDTO): boolean {
  if (t.status !== 'BOOKED') return false;
  const start = new Date(`${t.trainingDate}T${t.timeFrom}`);
  return this.isWithinActionWindow(start);
}

  noShowReservation(r: WorkerReservationDTO): void {
    this.reservationService.noShowReservation(r.id).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) this.loadData();
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while marking no-show.";
      }
    });
  }

  confirmTraining(t: WorkerTrainingDTO): void {
    this.reservationService.confirmTraining(t.id).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) this.loadData();
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while confirming training.";
      }
    });
  }

  noShowTraining(t: WorkerTrainingDTO): void {
    this.reservationService.noShowTraining(t.id).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) this.loadData();
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while marking no-show.";
      }
    });
  }
}
