import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class RatingService {
  private url = 'http://localhost:8080/ratings';
  private http = inject(HttpClient)


  addReaction(athleteId: number, facilityId: number, type: string) {
    const params = { athleteId, facilityId, type };
    return this.http.post<Message>(`${this.url}/addReaction`, null, { params: params as any });
  }

  addComment(athleteId: number, facilityId: number, comment: string) {
    return this.http.post<Message>(`${this.url}/addComment`, { athleteId, facilityId, comment });
  }

  getLast5Comments(facilityId: number, athleteId: number){
    return this.http.get<any[]>(`${this.url}/getLast5Comments`, { params: { facilityId, athleteId } as any });
  }

  getSummary(facilityId: number) {
    return this.http.get<{ likes: number; dislikes: number }>(`${this.url}/getSummary/${facilityId}`);
  }

  getMyComments(athleteId: number) {
    return this.http.get<any[]>(`${this.url}/getMyComments/${athleteId}`);
  }

  getMyReaction(athleteId: number, facilityId: number) {
    return this.http.get(`${this.url}/getMyReaction`, {
      params: { athleteId, facilityId } as any,
      responseType: 'text'
    });
}
}
