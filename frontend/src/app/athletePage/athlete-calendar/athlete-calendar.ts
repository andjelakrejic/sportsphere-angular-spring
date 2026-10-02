import { Component, OnInit, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Court } from '../../models/court';
import { Facility } from '../../models/facility';
import { ReservationService } from '../../services/reservation-service';


@Component({
  selector: 'app-athlete-calendar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './athlete-calendar.html',
  styleUrl: './athlete-calendar.css'
})
export class AthleteCalendar implements OnInit {
  @Input() courts: Court[] = [];
  @Input() facility!: Facility;
  @Input() athleteId!: number;

  currentCourtIndex = 0;
  weekStart: Date = this.getMonday(new Date());
  weekDays: Date[] = [];
  timeSlots: string[] = [];
  reservations: any[] = [];

  selectedDay: Date | null = null;
  selectedTime: string | null = null;
  message = '';

  private reservationService = inject(ReservationService);

  ngOnInit() {
    this.generateWeekDays();
    this.generateTimeSlots();
    this.loadReservations();
  }

  get currentCourt(): Court {
    return this.courts[this.currentCourtIndex];
  }

  // navigacija kroz terene
  prevCourt() {
    this.currentCourtIndex = this.currentCourtIndex === 0
      ? this.courts.length - 1
      : this.currentCourtIndex - 1;
    this.clearSelection();
    this.loadReservations();
  }

  nextCourt() {
    this.currentCourtIndex = this.currentCourtIndex === this.courts.length - 1
      ? 0
      : this.currentCourtIndex + 1;
    this.clearSelection();
    this.loadReservations();
  }

  // navigacija kroz nedelje
  prevWeek() {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() - 7);
    this.weekStart = d;
    this.generateWeekDays();
    this.clearSelection();
    this.loadReservations();
  }

  nextWeek() {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() + 7);
    this.weekStart = d;
    this.generateWeekDays();
    this.clearSelection();
    this.loadReservations();
  }

  getMonday(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  generateWeekDays() {
    this.weekDays = [];
    for (let i = 0; i < 7; i++) {
      const d = new Date(this.weekStart);
      d.setDate(this.weekStart.getDate() + i);
      this.weekDays.push(d);
    }
  }

  generateTimeSlots() {
    this.timeSlots = [];
    const from = parseInt(this.facility.workingHoursFrom.split(':')[0]);
    const to = parseInt(this.facility.workingHoursTo.split(':')[0]);
    for (let h = from; h < to; h++) {
      this.timeSlots.push(`${h.toString().padStart(2, '0')}:00`);
    }
  }

  loadReservations() {
    const weekEnd = new Date(this.weekStart);
    weekEnd.setDate(weekEnd.getDate() + 6);

    const startStr = this.formatDate(this.weekStart);
    const endStr = this.formatDate(weekEnd);

    const obj = {
      courtId: this.currentCourt.id,
      weekStart: startStr,
      weekEnd: endStr
    }

    this.reservationService.getReservationsForCourt(obj)
      .subscribe({
        next: (data) => this.reservations = data,
        error: () => this.reservations = []
      });
  }

  formatDate(d: Date): string {
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  // isSlotTaken(day: Date, time: string): boolean {
  //   return this.reservations.some(r => {
  //     const resDate = new Date(r.timeFrom);
  //     const resHour = resDate.getHours().toString().padStart(2, '0') + ':00';
  //     return resDate.toDateString() === day.toDateString() && resHour === time;
  //   });
  // }

  
  isSlotTaken(day: Date, time: string): boolean {
    const slotHour = parseInt(time.split(':')[0]);

    return this.reservations.some(r => {
      const resFrom = new Date(r.timeFrom);
      const resTo = new Date(r.timeTo);

      if (resFrom.toDateString() !== day.toDateString()) return false;

      const fromHour = resFrom.getHours();
      const toHour = resTo.getHours();

      // slot je zauzet ako njegov sat upada u [fromHour, toHour)
      return slotHour >= fromHour && slotHour < toHour;
    });
  }
  

  isSlotSelected(day: Date, time: string): boolean {
    return this.selectedDay?.toDateString() === day.toDateString() && this.selectedTime === time;
  }

  isPastSlot(day: Date, time: string): boolean {
    const now = new Date();
    const slotDate = new Date(day);
    const hour = parseInt(time.split(':')[0]);
    slotDate.setHours(hour, 0, 0, 0);
    return slotDate < now;
  }

  selectSlot(day: Date, time: string) {
    if (this.isSlotTaken(day, time) || this.isPastSlot(day, time)) return;
    this.selectedDay = day;
    this.selectedTime = time;
  }

  clearSelection() {
    this.selectedDay = null;
    this.selectedTime = null;
  }

  confirmReservation() {
    if (!this.selectedDay || !this.selectedTime) return;

    const startHour = parseInt(this.selectedTime.split(':')[0]);
    const endTime = `${(startHour + 1).toString().padStart(2, '0')}:00:00`;

    const obj = {
      courtId: this.currentCourt.id,
      athleteId: this.athleteId,
      sportId: this.currentCourt.sportId,
      date: this.formatDate(this.selectedDay),
      startTime: `${this.selectedTime}:00`,
      endTime: endTime
    };

    this.reservationService.createReservation(obj).subscribe({
      next: (msg) => {
        this.message = msg.message || 'Reservation created!';
        this.clearSelection();
        this.loadReservations();
      },
      error: () => {
        this.message = 'Error creating reservation.';
      }
    });
  }
}