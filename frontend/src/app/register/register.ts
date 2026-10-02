import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Athlete } from '../models/athlete';
import { Worker } from '../models/worker';
import { Sport } from '../models/sport';
import { FacilityService } from '../services/facility-service';
import { WorkerService } from '../services/worker-service';
import { FacilityWorkerOption } from '../models/facilityWorkerOption';

@Component({
  selector: 'app-register',
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register implements OnInit{

  selectedRole: 'ATHLETE' | 'EMPLOYEE' = 'ATHLETE';
  loading = false;
  errorMessage = '';
  successMessage = '';
  facilityId: number = 0

  
  allSports: Sport[] = [];
  selectedSports: Sport[] = [];

  selectedFile: File | null = null;
  previewUrl: string | null = null;

  formData = {
    ime: '',
    prezime: '',
    username: '',
    email: '',
    password: '',
    telefon: '',
    nazivObjekta: '',
    adresa: '',
    city: '',
    maticniBroj: '',
    pib: '',
    url: ''
  };

  private userService = inject(UserService)
  private workerService = inject(WorkerService)
  private facilityService = inject(FacilityService)

  availableFacilities: FacilityWorkerOption[] = [];
  selectedExistingFacilityId: number | null = null;


  ngOnInit() {
    this.facilityService.getAllSportsObject().subscribe(data => {
      if(data == null) console.log(data);
      else this.allSports = data;
    });

    this.workerService.getFacilitiesAvailableForSecondWorker().subscribe(data => {
      this.availableFacilities = data;
    });
  }

  onExistingFacilitySelected() {
    if (!this.selectedExistingFacilityId) {
      // korisnik je izabrao "Register new facility" opciju - očisti polja
      this.formData.nazivObjekta = '';
      this.formData.adresa = '';
      this.formData.city = '';
      this.formData.maticniBroj = '';
      this.formData.pib = '';
      return;
    }

    const facility = this.availableFacilities.find(f => f.id === this.selectedExistingFacilityId);
    if (facility) {
      this.formData.nazivObjekta = facility.name;
      this.formData.adresa = facility.address;
      this.formData.city = facility.city;
      this.formData.maticniBroj = facility.registrationNumber;
      this.formData.pib = facility.taxId;
    }
  }

  onSubmit() {
      this.errorMessage = '';
      this.successMessage = '';

      if (!this.validate()) return;

      this.loading = true;

      const formDataToSend = new FormData();
      formDataToSend.append('username', this.formData.username);
      formDataToSend.append('password', this.formData.password);
      formDataToSend.append('firstname', this.formData.ime);
      formDataToSend.append('lastname', this.formData.prezime);
      formDataToSend.append('email', this.formData.email);
      formDataToSend.append('phone', this.formData.telefon);
      formDataToSend.append('role', this.selectedRole);

      if (this.selectedFile) {
          formDataToSend.append('image', this.selectedFile);
      }

      if (this.selectedRole === 'ATHLETE') {
          formDataToSend.append('favoriteSports', JSON.stringify(this.selectedSports));
          this.userService.registerAthleteWithImage(formDataToSend).subscribe({
              next: () => {
                  this.successMessage = 'Registration request sent! Waiting for admin approval.';
                  this.loading = false;
              },
              error: (err) => {
                  this.errorMessage = err.error?.message || 'Something went wrong.';
                  this.loading = false;
              }
          });
      } else {
          formDataToSend.append('nameOfPlace', this.formData.nazivObjekta);
          formDataToSend.append('address', this.formData.adresa);
          formDataToSend.append('city', this.formData.city);
          formDataToSend.append('mb', this.formData.maticniBroj);
          formDataToSend.append('pib', this.formData.pib);

          if (this.selectedExistingFacilityId !== null) {
              formDataToSend.append('existingFacilityId', this.selectedExistingFacilityId.toString());
          }

          this.workerService.registerWorkerWithImage(formDataToSend).subscribe({
              next: () => {
                  this.successMessage = 'Registration request sent! Waiting for admin approval.';
                  this.loading = false;
              },
              error: (err) => {
                  this.errorMessage = err.error?.message || 'Something went wrong.';
                  this.loading = false;
              }
          });
      }
  }

  validate(): boolean {
    if (!this.formData.username || !this.formData.password || !this.formData.email) {
      this.errorMessage = 'Please fill in all required fields.';
      return false;
    }  

    const passwordRegex = /^(?=[A-Za-z])(?=.{8,12}$)(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z][A-Za-z0-9!@#$%^&*]*$/;
    if (!passwordRegex.test(this.formData.password)) {
      this.errorMessage = 'Password must be 8-12 chars, start with a letter, and contain at least one uppercase, one number, and one special character.';
      return false;
    }

    if (this.selectedRole === 'EMPLOYEE') {
      if (!this.formData.nazivObjekta || !this.formData.adresa || !this.formData.city) {
        this.errorMessage = 'Please fill in facility name, address, and city.';
        return false;
      }
      
      if (!/^\d{8}$/.test(this.formData.maticniBroj)) {
        this.errorMessage = 'Registration number must be exactly 8 digits.';
        return false;
      }
      if (!/^[1-9]\d{8}$/.test(this.formData.pib)) {
        this.errorMessage = 'Tax ID must be exactly 9 digits and cannot start with 0.';
        return false;
      }
    }

     if (this.selectedRole === 'ATHLETE' && this.selectedSports.length === 0) {
        this.errorMessage = 'Please select at least one favorite sport.';
        return false;
    }

    return true;
  }


  toggleSport(sport: Sport) {
    if (this.selectedSports.includes(sport)) {
        this.selectedSports = this.selectedSports.filter(s => s !== sport);
    } else {
        if (this.selectedSports.length >= 5) {
            this.errorMessage = 'You can select up to 5 sports.';
            return;
        }
        this.selectedSports.push(sport);
        this.errorMessage = '';
    }
  }

  isSelected(sport: Sport): boolean {
    return this.selectedSports.includes(sport);
  }


  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
        const file = input.files[0];

        // provera tipa fajla
        if (!file.type.startsWith('image/')) {
            this.errorMessage = 'Please select a valid image file.';
            return;
        }

        // provera veličine (npr. max 5MB)
        if (file.size > 5 * 1024 * 1024) {
            this.errorMessage = 'Image must be smaller than 5MB.';
            return;
        }

        this.selectedFile = file;
        this.errorMessage = '';

        // prikaz preview-a
        const reader = new FileReader();
        reader.onload = (e) => {
            this.previewUrl = e.target?.result as string;
        };
        reader.readAsDataURL(file);
    }
  }

  removeImage() {
    this.selectedFile = null;
    this.previewUrl = null;
  }

  generateAvatar(): void {
    if (!this.formData.ime || !this.formData.prezime) {
      this.errorMessage = 'Please enter your first and last name before generating an avatar.';
      return;
    }

    const canvas = document.createElement('canvas');
    canvas.width = 200;
    canvas.height = 200;
    const ctx = canvas.getContext('2d')!;

    const colors = ['#1a1a1a', '#3d3d3d', '#4a4a4a', '#5c5c5c', '#6b6b6b', '#8a8a8a'];
    const bgColor = colors[Math.floor(Math.random() * colors.length)];
    const initials = (this.formData.ime[0] + this.formData.prezime[0]).toUpperCase();

    // background circle
    ctx.fillStyle = bgColor;
    ctx.beginPath();
    ctx.arc(100, 100, 100, 0, Math.PI * 2);
    ctx.fill();

    // initials
    ctx.fillStyle = '#ffffff';
    ctx.font = 'bold 80px Arial';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    ctx.fillText(initials, 100, 105);

    canvas.toBlob((blob) => {
      if (blob) {
        const file = new File([blob], `avatar_${initials}_${Date.now()}.png`, { type: 'image/png' });
        this.selectedFile = file;
        this.previewUrl = canvas.toDataURL('image/png');
        this.errorMessage = '';
      }
    }, 'image/png');
  }
}
