import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../models/message';
import { WorkerReservationDTO } from '../models/workerResevationDTO';
import { WorkerTrainingDTO } from '../models/workerTrainingDTO';
import { AthleteBlockStatus } from '../models/athleteBlockStatus';

@Injectable({
  providedIn: 'root'
})
export class WorkerReservationService {

  private url = 'http://localhost:8080/worker-reservations';
  private http = inject(HttpClient)

  getReservations(facilityId: number) {
    return this.http.get<WorkerReservationDTO[]>(`${this.url}/getReservations/${facilityId}`);
  }

  getTrainings(facilityId: number) {
    return this.http.get<WorkerTrainingDTO[]>(`${this.url}/getTrainings/${facilityId}`);
  }

  confirmReservation(reservationId: number) {
    return this.http.post<Message>(`${this.url}/confirmReservation/${reservationId}`, {});
  }

  noShowReservation(reservationId: number) {
    return this.http.post<Message>(`${this.url}/noShowReservation/${reservationId}`, {});
  }

  confirmTraining(trainingId: number) {
    return this.http.post<Message>(`${this.url}/confirmTraining/${trainingId}`, {});
  }

  noShowTraining(trainingId: number) {
    return this.http.post<Message>(`${this.url}/noShowTraining/${trainingId}`, {});
  }

  getBlockStatus(athleteId: number, facilityId: number){
    return this.http.get<AthleteBlockStatus>( `${this.url}/block-status?athleteId=${athleteId}&facilityId=${facilityId}`);
  }
}