import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Athlete } from '../../models/athlete';
import { Reservation } from '../../models/reservation';
import { Message } from '../../models/message';
import { UserService } from '../../services/user-service';
import { DatePipe, SlicePipe } from '@angular/common';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { ReservationService } from '../../services/reservation-service';
import { IndividualTrainingService } from '../../services/individual-training-service';
import { IndividualTraining } from '../../models/individualTraining';
import { FacilityService } from '../../services/facility-service';
import { Sport } from '../../models/sport';

type SortColumn = 'facilityName' | 'city' | 'courtName' | 'sport' | 'timeFrom' | 'status';

@Component({
  selector: 'app-athlete-profile',
  imports: [FormsModule, DatePipe, AthleteNavBar, SlicePipe],
  templateUrl: './athlete-profile.html',
  styleUrl: './athlete-profile.css',
})

export class AthleteProfile {
  private router = inject(Router)

  athlete: Athlete = new Athlete()
  favoriteSports: Sport[] = []
  reservations: Reservation[] = []
  trainings: IndividualTraining[] = []
  allSports: Sport[] = []
  selectedSportToAdd: number | null = null

  message1: Message | null = null
  message2: Message | null = null
  message3: Message | null = null
  message4: Message = new Message()
  selectedImageFile: File | null = null
  imagePreview: string | null = null

  oldPassword: string = ''
  newPassword: string = ''
  confirmPassword: string = ''

  sortColumn: SortColumn | null = null
  sortDirection: 'asc' | 'desc' = 'asc'

  private userService = inject(UserService)
  private reservationService = inject(ReservationService)
  private trainingService = inject(IndividualTrainingService)
  private facilityService = inject(FacilityService)

  ngOnInit(): void {
    this.athlete = JSON.parse(localStorage.getItem("athlete")!)
    if (this.athlete == null) alert("No athlete in local storage")

    this.reservationService.getReservations(this.athlete.id).subscribe({
      next: (data) => this.reservations = data,
      error: (err) => console.error("Failed to fetch reservations", err)
    })

    this.trainingService.getAthleteTrainings(this.athlete.id).subscribe({
      next: (data) => {
        this.trainings = data
      },
      error: (err) => console.error("Failed to fetch trainings", err)
    })

    this.facilityService.getAllSportsObject().subscribe({
      next: (data) => this.allSports = data,
      error: (err) => console.error("Failed to fetch sports", err)
    })

    this.reloadSports()
  }

  get profileImageUrl(): string {
    if (!this.athlete.profileImage) return 'assets/default-avatar.png'; // fallback iz frontend assets foldera
    return `http://localhost:8080/images/${this.athlete.profileImage}`;
  }

  get availableSportsToAdd(): Sport[] {
    return this.allSports.filter(s => !this.favoriteSports.some(fs => fs.id === s.id));
  }

  reloadSports() {
    this.userService.getFavoriteSports(this.athlete.id).subscribe({
      next: (data) => this.favoriteSports = data,
      error: (err) => console.error("Error getting athlete's fav sports: ", err)
    })
  }

  addSport() {
    if (!this.selectedSportToAdd) return;
    const sport = this.allSports.find(s => s.id === this.selectedSportToAdd);
    if (!sport) return;
    this.favoriteSports.push(sport);
    this.selectedSportToAdd = null;
    this.saveFavoriteSports();
  }

  removeSport(sport: Sport) {
    this.favoriteSports = this.favoriteSports.filter(s => s.id !== sport.id);
    this.saveFavoriteSports();
  }

  saveFavoriteSports() {
    const data = {
      athleteId: this.athlete.id,
      sportIds: this.favoriteSports.map(s => s.id)
    };

    this.userService.updateFavoriteSports(data).subscribe({
      next: (msg) => {
        this.message3 = msg;
        this.reloadSports();
      },
      error: (err) => console.error(err)
    });
  }

  get sortedReservations(): Reservation[] {
    if (!this.sortColumn) return this.reservations;

    const col = this.sortColumn;
    const dir = this.sortDirection === 'asc' ? 1 : -1;

    return [...this.reservations].sort((a: any, b: any) => {
      let valA = a[col];
      let valB = b[col];

      if (col === 'timeFrom') {
        valA = new Date(valA).getTime();
        valB = new Date(valB).getTime();
      } else if (typeof valA === 'string') {
        valA = valA.toLowerCase();
        valB = valB.toLowerCase();
      }

      if (valA < valB) return -1 * dir;
      if (valA > valB) return 1 * dir;
      return 0;
    });
  }

  sortBy(column: SortColumn) {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
  }

  saveChanges(){
    this.userService.updateAthlete(this.athlete).subscribe(data => {
      this.message1 = data
      this.athlete.favoriteSports = this.favoriteSports
      localStorage.setItem('athlete', JSON.stringify(this.athlete))
    })
  }

  changePassword() {
    this.message4.message = "";

    if (!this.oldPassword || !this.newPassword || !this.confirmPassword) {
      this.message4.message = "Please fill in all password fields"
      this.message4.success = false
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      this.message4.message = "New passwords do not match"
      this.message4.success = false

      return;
    }

    const passwordRegex = /^(?=[A-Za-z])(?=.{8,12}$)(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z][A-Za-z0-9!@#$%^&*]*$/;
    if (!passwordRegex.test(this.newPassword)) {
      this.message4.message = "Password must be 8-12 chars, start with a letter, and contain at least one uppercase, one number, and one special character."
      this.message4.success = false
      return;
    }

    if (this.newPassword === this.oldPassword) {
      this.message4.message = "New password must be different from the current password"
      this.message4.success = false
      return;
    }

    this.userService.changePassword(this.athlete.username, this.oldPassword, this.newPassword).subscribe({
      next: (data) => {
        this.message4 = data;
        if (data.success) {
          this.oldPassword = '';
          this.newPassword = '';
          this.confirmPassword = '';
        }
      },
      error: (err) => {
        console.error("Password change failed", err);
        this.message4.message = "Something went wrong. Please try again."
        this.message4.success = false
      }
    });
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];

    const reader = new FileReader();
    reader.onload = () => this.imagePreview = reader.result as string;
    reader.readAsDataURL(file);

    this.userService.uploadProfileImage(this.athlete.username, file).subscribe({
      next: (filename) => {
        this.athlete.profileImage = filename;
        localStorage.setItem('athlete', JSON.stringify(this.athlete));
        this.imagePreview = null;
      },
      error: (err) => console.error("Image upload failed", err)
    });
  }

  canCancel(timeFrom: Date | string): boolean {
    const date = new Date(timeFrom);
    const diff = date.getTime() - new Date().getTime();
    return diff >= 12 * 60 * 60 * 1000;
  }

  cancelReservation(id: number){
    this.reservationService.cancelReservation(id).subscribe(data => {
      this.message2 = data
      this.ngOnInit()
    })
  }

  getReservationStatusDisplay(r: Reservation): { icon: string; text: string; cssClass: string } {
    const isFuture = new Date(r.timeFrom).getTime() > new Date().getTime();

    if (r.status === 'BOOKED' && isFuture) {
      return { icon: '📅', text: 'Scheduled', cssClass: 'booked' };
    }
    if (r.status === 'BOOKED' && !isFuture) {
      // termin je pocela ali jos nije obradjen (u prozoru od 10 min) - retko ce se videti
      return { icon: '⏳', text: 'Awaiting confirmation', cssClass: 'pending' };
    }
    if (r.status === 'CONFIRMED') {
      return { icon: '✅', text: 'Completed', cssClass: 'confirmed' };
    }
    if (r.status === 'NO_SHOW') {
      return { icon: '❌', text: 'Missed', cssClass: 'no-show' };
    }
    if (r.status === 'CANCELLED') {
      return { icon: '🚫', text: 'Cancelled', cssClass: 'cancelled' };
    }
    return { icon: '', text: r.status, cssClass: '' };
  }
}