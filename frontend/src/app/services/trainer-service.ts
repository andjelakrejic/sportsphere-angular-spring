import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Trainer } from '../models/trainer';

@Injectable({
  providedIn: 'root',
})
export class TrainerService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/trainers';

  getTrainersByFacilityAndSport(facilityId: number, sportId: number) {
    return this.http.get<Trainer[]>(`${this.url}/getTrainersByFacilityAndSport`, {
      params: { facilityId, sportId }
    });
  }

  getAllTrainers() {
    return this.http.get<Trainer[]>(`${this.url}/getAllTrainers`);
  }
}
