import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IndividualTraining } from '../models/individualTraining';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class IndividualTrainingService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/individual-trainings';

  bookTraining(training: Partial<IndividualTraining>) {
    return this.http.post<boolean>(`${this.url}/bookTraining`, training);
  }

  getAthleteTrainings(athleteId: number) {
    return this.http.get<IndividualTraining[]>(`${this.url}/athlete/${athleteId}`);
  }

  getTrainingsForCourt(obj: any) {
    return this.http.post<any[]>(`${this.url}/getTrainingsForCourt`, obj);
  }

  updateTrainingTime(obj: any) {
    return this.http.post<Message>(`${this.url}/updateTrainingTime`, obj);
  }
}
