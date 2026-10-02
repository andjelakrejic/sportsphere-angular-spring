import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Worker } from '../../models/worker';
import { Court } from '../../models/court';
import { Message } from '../../models/message';
import { WorkerService } from '../../services/worker-service';
import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { Facility } from '../../models/facility';
import { FacilityService } from '../../services/facility-service';

interface FacilityWithCourts {
  facility: Facility;
  openCourts: Court[];
  closedCourts: Court[];
  halls: Court[];
  hasValidOpenCourt: boolean;
  closedNamesUnique: boolean;
  hallNamesUnique: boolean;
}

@Component({
  selector: 'app-worker-profile',
  imports: [FormsModule, WorkerNavBar],
  templateUrl: './worker-profile.html',
  styleUrl: './worker-profile.css',
})
export class WorkerProfile {
  private workerService = inject(WorkerService);

  worker: Worker = new Worker();
  facilities: FacilityWithCourts[] = [];

  message1: Message | null = null;
  imagePreview: string | null = null;

  ngOnInit(): void {
    this.worker = JSON.parse(localStorage.getItem("worker")!);
    if (this.worker == null) { alert("No worker in local storage"); return; }

    this.loadFacility();
  }

 private facilityService = inject(FacilityService);

  loadFacility() {
    this.workerService.getMyFacilities(this.worker.id).subscribe({
      next: (data) => {
        this.facilities = data;
        this.facilities.forEach(item => this.loadSportsForFacility(item));
      },
      error: (err) => console.error("Failed to fetch facilities", err)
    });
  }

  loadSportsForFacility(item: FacilityWithCourts) {
    this.facilityService.getSportsForFacility(item.facility.id).subscribe({
      next: (sports) => {
        item.facility.sports = sports.map(s => s.name).join(', ');
      },
      error: (err) => console.error("Failed to fetch sports for facility", item.facility.id, err)
    });
  }

  get profileImageUrl(): string {
    if (!this.worker.profileImage) return 'assets/default-avatar.png';
    return `http://localhost:8080/images/${this.worker.profileImage}`;
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];

    const reader = new FileReader();
    reader.onload = () => this.imagePreview = reader.result as string;
    reader.readAsDataURL(file);

    this.workerService.uploadProfileImage(this.worker.username, file).subscribe({
      next: (filename) => {
        this.worker.profileImage = filename;
        localStorage.setItem('worker', JSON.stringify(this.worker));
        this.imagePreview = null;
      },
      error: (err) => console.error("Image upload failed", err)
    });
  }

  saveChanges() {
    this.workerService.updateWorker(this.worker).subscribe(data => {
      this.message1 = data;
      localStorage.setItem('worker', JSON.stringify(this.worker));
    });
  }
}