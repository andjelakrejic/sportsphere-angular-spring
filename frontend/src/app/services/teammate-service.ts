import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { TeammateAd } from '../models/teammateAd';
import { TeammateRequest } from '../models/teammateRequest';
import { Message } from '../models/message';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TeammateService {
  private http = inject(HttpClient)
  private url = "http://localhost:8080/teammates"


    // Request
    getTeammateRequests(adId: number) {
      return this.http.get<TeammateRequest[]>(`${this.url}/getTeammateRequests/${adId}`)
    }

    getMySentRequestsWithStatus(athleteId: number) {
      return this.http.get<TeammateRequest[]>(`${this.url}/getMySentRequestsWithStatus/${athleteId}`);
    }

    getTeammateRequest(athleteId: number, adId: number): Observable<TeammateRequest> {
      const data = new HttpParams()
        .set('athleteId', athleteId.toString())
        .set('adId', adId.toString());

      return this.http.get<TeammateRequest>(`${this.url}/getTeammateRequest`, { 
          params: data 
      });    
    }

    rejectRequest(requestId: number){
      return this.http.post<Message>(`${this.url}/rejectRequest?requestId=${requestId}`, null);
    }

    sendRequest(adId: number, athleteId: number){
      const data = {
        athleteId: athleteId,
        adId: adId
      }
      return this.http.post<Message>(`${this.url}/sendRequest`, data)
    }

    approveRequest(requestId: number, adId: number){
      const data = {
        id: requestId,
        adId: adId
      }
      return this.http.post<Message>(`${this.url}/approveRequest`, data)
    }



    // Ad
    getTeammateAds(athleteId: number) {
      return this.http.get<TeammateAd[]>(`${this.url}/getTeammateAds/${athleteId}`)
    }

    getAllActiveAds() {
      return this.http.get<TeammateAd[]>(`${this.url}/getAllActiveAds`)
    }

    closeAd(adId: number) {
      return this.http.post<Message>(`${this.url}/closeAd`, adId)
    }

    createAd(athleteId: number, sportId: number, city: string, date: string, timeSlot: string, totalPlayersNeeded: number) {
      const data = {
        athleteId: athleteId,
        sportId: sportId,
        city: city,
        date: date,
        timeSlot: timeSlot,
        totalPlayersNeeded: totalPlayersNeeded
      } 
      return this.http.post<Message>(`${this.url}/createAd`, data)
    }

    getMySentRequests(athleteId: number) {
      return this.http.get<number[]>(`${this.url}/getMySentRequests/${athleteId}`);
    }
  
    getMyTeams(athleteId: number) {
      return this.http.get<TeammateAd[]>(`${this.url}/getMyTeams/${athleteId}`);
    }

    getApprovedPlayers(adId: number) {
      return this.http.get<TeammateRequest[]>(`${this.url}/getApprovedPlayers/${adId}`);
    }

    removePlayerFromTeam(adId: number, athleteId: number){
      const data = {
        adId: adId,
        athleteId: athleteId
      }
      return this.http.post<Message>(`${this.url}/removePlayerFromTeam`, data)
    }
}
