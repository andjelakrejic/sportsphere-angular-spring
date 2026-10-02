import { Component, inject, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Facility } from '../../models/facility';
import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { FacilityService } from '../../services/facility-service';
import { ReportService } from '../../services/report-service';
import { Worker } from '../../models/worker';

@Component({
  selector: 'app-worker-report',
  standalone: true,
  imports: [CommonModule, FormsModule, WorkerNavBar],
  templateUrl: './worker-report.html',
  styleUrl: './worker-report.css'
})
export class WorkerReport implements OnInit {
  worker: Worker = new Worker()

  facilities: Facility[] = [];
  selectedFacility: Facility | null = null;

  selectedMonth: string = this.getCurrentMonth();
  message = '';
  loadingOccupancy = false;
  loadingEquipment = false;

  private facilityService = inject(FacilityService);
  private reportService = inject(ReportService);


  ngOnInit() {
    this.worker = JSON.parse(localStorage.getItem("worker")!)
    this.facilityService.getFacilitiesByWorker(this.worker.id).subscribe({
      next: (data) => {
        this.facilities = data;
        if (data.length > 0) this.selectedFacility = data[0];
      },
      error: () => this.facilities = []
    });
  }

  getCurrentMonth(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    return `${year}-${month}`;
  }

  downloadOccupancy() {
    if (!this.selectedFacility) {
      this.message = 'Please select a facility.';
      return;
    }
    this.loadingOccupancy = true;
    this.reportService.downloadOccupancyReport(this.selectedFacility.id, this.selectedMonth)
      .subscribe({
        next: (blob) => {
          this.triggerDownload(blob, 'occupancy-report.pdf');
          this.loadingOccupancy = false;
        },
        error: () => {
          this.message = 'Error generating report.';
          this.loadingOccupancy = false;
        }
      });
  }

  downloadEquipment() {
    this.loadingEquipment = true;
    this.reportService.downloadEquipmentReport(this.selectedMonth)
      .subscribe({
        next: (blob) => {
          this.triggerDownload(blob, 'equipment-report.pdf');
          this.loadingEquipment = false;
        },
        error: () => {
          this.message = 'Error generating report.';
          this.loadingEquipment = false;
        }
      });
  }

  private triggerDownload(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}