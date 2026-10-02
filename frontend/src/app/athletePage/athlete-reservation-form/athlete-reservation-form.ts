import { Component, OnInit, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Court } from '../../models/court';
import { Facility } from '../../models/facility';
import { ReservationService } from '../../services/reservation-service';

@Component({
  selector: 'app-athlete-reservation-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './athlete-reservation-form.html',
  styleUrl: './athlete-reservation-form.css'
})
export class AthleteReservationForm implements OnInit {
  @Input() courts: Court[] = [];
  @Input() facility!: Facility;
  @Input() athleteId!: number;

  isOpen = false;

  selectedCourtId: number | null = null;
  selectedDate: string = '';
  minDate: string = '';

  timeSlots: string[] = [];
  reservations: any[] = [];

  availableStartTimes: string[] = [];
  selectedStartTime: string | null = null;

  durationOptions: number[] = [];
  selectedDuration: number | null = null;

  message = '';
  errorMsg = '';

  private reservationService = inject(ReservationService);

  ngOnInit() {
    this.generateTimeSlots();
    this.minDate = this.formatDate(new Date());
    this.selectedDate = this.minDate;

    if (this.courts.length > 0) {
      this.selectedCourtId = this.courts[0].id;
    }
  }

  openForm() {
    this.isOpen = true;
    this.message = '';
    this.errorMsg = '';
    this.onCourtOrDateChange();
  }

  closeForm() {
    this.isOpen = false;
  }

  get selectedCourt(): Court | undefined {
    return this.courts.find(c => c.id === this.selectedCourtId);
  }

  generateTimeSlots() {
    this.timeSlots = [];
    const from = parseInt(this.facility.workingHoursFrom.split(':')[0]);
    const to = parseInt(this.facility.workingHoursTo.split(':')[0]);
    for (let h = from; h < to; h++) {
      this.timeSlots.push(`${h.toString().padStart(2, '0')}:00`);
    }
  }

  formatDate(d: Date): string {
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  getMonday(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  onCourtOrDateChange() {
    this.selectedStartTime = null;
    this.selectedDuration = null;
    this.durationOptions = [];
    this.errorMsg = '';
    this.message = '';

    if (!this.selectedCourtId || !this.selectedDate) {
      this.availableStartTimes = [];
      return;
    }

    const selected = new Date(this.selectedDate);
    const weekStart = this.getMonday(selected);
    const weekEnd = new Date(weekStart);
    weekEnd.setDate(weekEnd.getDate() + 6);

    const obj = {
      courtId: this.selectedCourtId,
      weekStart: this.formatDate(weekStart),
      weekEnd: this.formatDate(weekEnd)
    };

    this.reservationService.getReservationsForCourt(obj).subscribe({
      next: (data) => {
        this.reservations = data;
        this.computeAvailableStartTimes();
      },
      error: () => {
        this.reservations = [];
        this.computeAvailableStartTimes();
      }
    });
  }

  isSlotTaken(time: string): boolean {
    const selected = new Date(this.selectedDate);
    return this.reservations.some(r => {
      const resDate = new Date(r.timeFrom);
      const resHour = resDate.getHours().toString().padStart(2, '0') + ':00';
      return resDate.toDateString() === selected.toDateString() && resHour === time;
    });
  }

  isPastSlot(time: string): boolean {
    const now = new Date();
    const slotDate = new Date(this.selectedDate);
    const hour = parseInt(time.split(':')[0]);
    slotDate.setHours(hour, 0, 0, 0);
    return slotDate < now;
  }

  computeAvailableStartTimes() {
    this.availableStartTimes = this.timeSlots.filter(t => !this.isSlotTaken(t) && !this.isPastSlot(t));
  }

  onStartTimeChange() {
    this.selectedDuration = null;
    this.durationOptions = [];

    if (!this.selectedStartTime) return;

    const startIndex = this.timeSlots.indexOf(this.selectedStartTime);
    let maxDuration = 0;

    // broji koliko je uzastopnih slobodnih termina od izabranog pocetka
    for (let i = startIndex; i < this.timeSlots.length; i++) {
      if (this.isSlotTaken(this.timeSlots[i])) break;
      maxDuration++;
    }

    this.durationOptions = Array.from({ length: maxDuration }, (_, i) => i + 1);
    this.selectedDuration = 1;
  }

  get endTimePreview(): string {
    if (!this.selectedStartTime || !this.selectedDuration) return '';
    const startHour = parseInt(this.selectedStartTime.split(':')[0]);
    const endHour = startHour + this.selectedDuration;
    return `${endHour.toString().padStart(2, '0')}:00`;
  }

  submitReservation() {
    this.errorMsg = '';
    this.message = '';

    if (!this.selectedCourtId || !this.selectedDate || !this.selectedStartTime || !this.selectedDuration) {
      this.errorMsg = 'Please fill in all fields.';
      return;
    }

    const court = this.selectedCourt;
    if (!court) {
      this.errorMsg = 'Selected court not found.';
      return;
    }

    const startHour = parseInt(this.selectedStartTime.split(':')[0]);
    const endHour = startHour + this.selectedDuration;

    const obj = {
      courtId: this.selectedCourtId,
      athleteId: this.athleteId,
      sportId: court.sportId,
      date: this.selectedDate,
      startTime: `${this.selectedStartTime}:00`,
      endTime: `${endHour.toString().padStart(2, '0')}:00:00`
    };

    this.reservationService.createReservation(obj).subscribe({
      next: (msg) => {
        this.message = msg.message || 'Reservation created!';
        this.selectedStartTime = null;
        this.selectedDuration = null;
        this.onCourtOrDateChange();
      },
      error: (err) => {
        this.errorMsg = err.error?.message || 'Error creating reservation.';
      }
    });
  }
}