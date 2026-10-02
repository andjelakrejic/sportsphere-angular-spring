import { Component, inject, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DragDropModule, CdkDragDrop } from '@angular/cdk/drag-drop';
import { Facility } from '../../models/facility';
import { Court } from '../../models/court';

import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { FacilityService } from '../../services/facility-service';
import { CourtService } from '../../services/court-service';
import { ReservationService } from '../../services/reservation-service';
import { IndividualTrainingService } from '../../services/individual-training-service';
import { Worker } from '../../models/worker';

interface ScheduleItem {
  id: number;
  type: 'RESERVATION' | 'TRAINING';
  timeFrom: string;
  timeTo: string;
  status: string;
}

@Component({
  selector: 'app-worker-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule, DragDropModule, WorkerNavBar],
  templateUrl: './worker-calendar.html',
  styleUrl: './worker-calendar.css'
})
export class WorkerCalendar implements OnInit {
  @Input() workerId!: number;

  worker: Worker = new Worker()

  facilities: Facility[] = [];
  selectedFacility: Facility | null = null;

  courts: Court[] = [];
  selectedCourt: Court | null = null;

  weekStart: Date = this.getMonday(new Date());
  weekDays: Date[] = [];
  timeSlots: string[] = [];

  scheduleItems: ScheduleItem[] = [];
  message = '';

  private facilityService = inject(FacilityService);
  private courtService = inject(CourtService);
  private reservationService = inject(ReservationService);
  private trainingService = inject(IndividualTrainingService);

  ngOnInit() {
    this.worker = JSON.parse(localStorage.getItem("worker")!)
    this.generateWeekDays();
    this.loadFacilities();
  }

  loadFacilities() {
    this.facilityService.getFacilitiesByWorker(this.worker.id).subscribe({
      next: (data) => {
        this.facilities = data;
        if (data.length > 0) {
          this.selectedFacility = data[0];
          this.onFacilityChange();
        }
      },
      error: () => this.facilities = []
    });
  }

  onFacilityChange() {
    if (!this.selectedFacility) return;
    this.generateTimeSlots();
    this.courtService.getCourtsForFacility(this.selectedFacility.id).subscribe({
      next: (data) => {
        this.courts = data;
        this.selectedCourt = data.length > 0 ? data[0] : null;
        this.loadSchedule();
      },
      error: () => { this.courts = []; this.selectedCourt = null; }
    });
  }

  onCourtChange() {
    this.loadSchedule();
  }

  get isDraggable(): boolean {
    return this.selectedCourt?.type === 'CLOSED' || this.selectedCourt?.type === 'HALL';
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
    if (!this.selectedFacility) return;
    const from = parseInt(this.selectedFacility.workingHoursFrom.split(':')[0]);
    const to = parseInt(this.selectedFacility.workingHoursTo.split(':')[0]);
    for (let h = from; h < to; h++) {
      this.timeSlots.push(`${h.toString().padStart(2, '0')}:00`);
    }
  }

  prevWeek() {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() - 7);
    this.weekStart = d;
    this.generateWeekDays();
    this.loadSchedule();
  }

  nextWeek() {
    const d = new Date(this.weekStart);
    d.setDate(d.getDate() + 7);
    this.weekStart = d;
    this.generateWeekDays();
    this.loadSchedule();
  }

  formatDate(d: Date): string {
    const year = d.getFullYear();
    const month = (d.getMonth() + 1).toString().padStart(2, '0');
    const day = d.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  loadSchedule() {
    if (!this.selectedCourt) { this.scheduleItems = []; return; }

    const weekEnd = new Date(this.weekStart);
    weekEnd.setDate(weekEnd.getDate() + 6);

    const obj = {
      courtId: this.selectedCourt.id,
      weekStart: this.formatDate(this.weekStart),
      weekEnd: this.formatDate(weekEnd)
    };

    this.reservationService.getReservationsForCourt(obj).subscribe({
      next: (data: any[]) => {
        const tagged: ScheduleItem[] = data.map(r => ({ ...r, type: 'RESERVATION' }));
        this.scheduleItems = [...this.scheduleItems.filter(i => i.type !== 'RESERVATION'), ...tagged];
      },
      error: () => {}
    });

    this.trainingService.getTrainingsForCourt(obj).subscribe({
      next: (data: any[]) => {
        const tagged: ScheduleItem[] = data.map(t => ({ ...t, type: 'TRAINING' }));
        this.scheduleItems = [...this.scheduleItems.filter(i => i.type !== 'TRAINING'), ...tagged];
      },
      error: () => {}
    });
  }

  getItem(day: Date, time: string): ScheduleItem | null {
    return this.scheduleItems.find(item => {
      const itemDate = new Date(item.timeFrom);
      const itemHour = itemDate.getHours().toString().padStart(2, '0') + ':00';
      return itemDate.toDateString() === day.toDateString() && itemHour === time;
    }) || null;
  }

  dropListId(day: Date, time: string): string {
    return `${day.toDateString()}_${time}`;
  }

  getAllDropListIds(): string[] {
    const ids: string[] = [];
    for (const day of this.weekDays) {
      for (const time of this.timeSlots) {
        ids.push(this.dropListId(day, time));
      }
    }
    return ids;
  }

  onDrop(event: CdkDragDrop<any>, targetDay: Date, targetTime: string) {
    const item: ScheduleItem = event.item.data;
    if (!item) return;

    if (this.getItem(targetDay, targetTime)) {
      this.message = 'Taj termin je već zauzet.';
      return;
    }

    const startHour = parseInt(targetTime.split(':')[0]);
    const durationHours = (new Date(item.timeTo).getTime() - new Date(item.timeFrom).getTime()) / (1000 * 60 * 60);
    const endHour = startHour + durationHours;

    const updateObj = {
      id: item.id,
      date: this.formatDate(targetDay),
      startTime: `${startHour.toString().padStart(2, '0')}:00:00`,
      endTime: `${Math.floor(endHour).toString().padStart(2, '0')}:00:00`
    };

    const request$ = item.type === 'RESERVATION'
      ? this.reservationService.updateReservationTime(updateObj)
      : this.trainingService.updateTrainingTime(updateObj);

    request$.subscribe({
      next: (msg: any) => {
        this.message = msg.message || 'Termin uspešno premešten!';
        this.loadSchedule();
      },
      error: () => this.message = 'Greška prilikom premeštanja termina.'
    });
  }

  getMonday(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    d.setDate(diff);
    d.setHours(0, 0, 0, 0);
    return d;
  }
}