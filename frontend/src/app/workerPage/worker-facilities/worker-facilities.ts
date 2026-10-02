import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Facility } from '../../models/facility';
import { Court } from '../../models/court';
import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { FacilityUploadDTO } from '../../models/facilityUploadDTO';
import { FacilityService } from '../../services/facility-service';
import { Sport } from '../../models/sport';

@Component({
  selector: 'app-worker-facilities',
  imports: [CommonModule, FormsModule, WorkerNavBar],
  templateUrl: './worker-facilities.html',
  styleUrl: './worker-facilities.css'
})
export class WorkerFacilities implements OnInit {

  workerId: number = 0;

  facilities: Facility[] = [];
  selectedFacilityId: number | null = null;
  courts: Court[] = [];
  sports: Sport[] = []

  openCourts: Court[] = [];
  closedCourts: Court[] = [];
  hallCourts: Court[] = [];

  hasValidOpenCourt: boolean = false;

  showAddFacilityForm: boolean = false;
  newFacility: FacilityUploadDTO = new FacilityUploadDTO();

  showAddCourtForm: boolean = false;
  newCourt: Court = new Court();

  selectedFile: File | null = null;

  feedbackMessage: string = "";
  feedbackSuccess: boolean = false;
  newCourtError: string = "";

  showEditFacilityForm: boolean = false;
  editFacility: Facility = new Facility();

  private facilityService = inject(FacilityService) 

  ngOnInit(): void {
    const worker = JSON.parse(localStorage.getItem('worker') || 'null');
    this.workerId = worker ? worker.id : 0;
    this.loadFacilities();
    this.loadAllSports(); 
  }

  loadAllSports(): void {
    this.facilityService.getAllSportsObject().subscribe({
      next: (data) => this.sports = data,
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load sports list.";
      }
    });
  }

  loadFacilities(): void {
    if (!this.workerId) return;

    this.facilityService.getFacilitiesByWorker(this.workerId).subscribe({
      next: (data) => {
        this.facilities = data;
        if (this.facilities.length > 0 && this.selectedFacilityId === null) {
          this.selectedFacilityId = this.facilities[0].id;
          this.loadCourts();
        }
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load facilities.";
      }
    });
  }

  getSportName(sportId: number): string {
    const sport = this.sports.find(s => s.id === sportId);
    return sport ? sport.name : "-";
  }

  loadCourts(): void {
    if (this.selectedFacilityId === null) return;

    this.facilityService.getCourtsByFacility(this.selectedFacilityId).subscribe({
      next: (data) => {
        this.courts = data;
        this.groupCourts();
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Failed to load courts.";
      }
    });
  }

  onFacilityChange(): void {
    this.loadCourts();
  }

  groupCourts(): void {
    this.openCourts = this.courts.filter(c => c.type === 'OPEN');
    this.closedCourts = this.courts.filter(c => c.type === 'CLOSED');
    this.hallCourts = this.courts.filter(c => c.type === 'HALL');
    this.hasValidOpenCourt = this.openCourts.some(c => c.capacity >= 4);
  }

  get selectedFacility(): Facility | undefined {
    return this.facilities.find(f => f.id === this.selectedFacilityId);
  }

  toggleAddFacilityForm(): void {
    this.showAddFacilityForm = !this.showAddFacilityForm;
    this.newFacility = new FacilityUploadDTO();
  }

  submitNewFacility(): void {
    if (!this.newFacility.name || !this.newFacility.city || !this.newFacility.address) {
      this.feedbackSuccess = false;
      this.feedbackMessage = "Name, city and address are required.";
      return;
    }

    this.facilityService.addFacility(this.newFacility, this.workerId).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) {
          this.showAddFacilityForm = false;
          this.loadFacilities();
        }
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while adding facility.";
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files && input.files.length > 0 ? input.files[0] : null;
  }

  submitJsonUpload(): void {
    if (!this.selectedFile) {
      this.feedbackSuccess = false;
      this.feedbackMessage = "Please select a JSON file first.";
      alert("Please select a JSON file first!");
      return;
    }

    this.facilityService.uploadFacilityJson(this.selectedFile, this.workerId).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;

        if (res.success) {
          alert("Facility created successfully and submitted for approval!");
          this.selectedFile = null;
          
          // Clear the file input in HTML if present
          const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
          if (fileInput) fileInput.value = '';
          
          this.loadFacilities();
        } else {
          // Validation error or bad JSON contents returned from server
          alert("Error processing JSON file:\n" + res.message);
        }
      },
      error: (err) => {
        this.feedbackSuccess = false;
        const errorMessage = err.error?.message || "Server error while uploading file.";
        this.feedbackMessage = errorMessage;
        alert("Server error while uploading file:\n" + errorMessage);
      }
    });
  }

  toggleAddCourtForm(): void {
    this.showAddCourtForm = !this.showAddCourtForm;
    this.newCourt = new Court();
    if (this.selectedFacilityId !== null) {
      this.newCourt.facilityId = this.selectedFacilityId;
    }
  }

  submitNewCourt(): void {
    this.newCourtError = "";

    if (this.selectedFacilityId === null) {
      this.newCourtError = "Select a facility first.";
      return;
    }

    if (!this.newCourt.name || !this.newCourt.type || this.newCourt.capacity <= 0 || this.newCourt.sportId === 0) {
      this.newCourtError = "Name, type, capacity and sport are required.";
      return;
    }

    if (this.newCourt.equipmentDescription.length > 300) {
      this.newCourtError = "Equipment description must be at most 300 characters.";
      return;
    }

    this.newCourt.facilityId = this.selectedFacilityId;

    this.facilityService.addCourt(this.newCourt).subscribe({
      next: (res) => {
        if (res.success) {
          this.showAddCourtForm = false;
          this.loadCourts();
          this.feedbackSuccess = true;
          this.feedbackMessage = res.message;
        } else {
          this.newCourtError = res.message;
        }
      },
      error: () => {
        this.newCourtError = "Server error while adding court.";
      }
    });
  }


  toggleEditFacilityForm(): void {
    this.showEditFacilityForm = !this.showEditFacilityForm;
    if (this.showEditFacilityForm && this.selectedFacility) {
      // popuni formu trenutnim podacima izabranog objekta
      this.editFacility = { ...this.selectedFacility };
    }
  }

  submitEditFacility(): void {
    if (!this.editFacility.name || !this.editFacility.city || !this.editFacility.address) {
      this.feedbackSuccess = false;
      this.feedbackMessage = "Name, city and address are required.";
      return;
    }

    this.facilityService.updateFacility(this.editFacility).subscribe({
      next: (res) => {
        this.feedbackSuccess = res.success;
        this.feedbackMessage = res.message;
        if (res.success) {
          this.showEditFacilityForm = false;
          this.loadFacilities();
        }
      },
      error: () => {
        this.feedbackSuccess = false;
        this.feedbackMessage = "Server error while updating facility.";
      }
    });
  }
}