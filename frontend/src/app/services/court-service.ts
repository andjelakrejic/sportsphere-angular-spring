import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Court } from '../models/court';

@Injectable({
  providedIn: 'root',
})
export class CourtService {
  private http = inject(HttpClient)
  private url = "http://localhost:8080/courts"


  getAvailableCourts(facilityId: number) {
    return this.http.get<Court[]>(`${this.url}/getAvailableCourts/${facilityId}`);
  }

  getCourtsForFacility(facilityId: number){
    return this.http.get<Court[]>(`${this.url}/getCourtsForFacility/${facilityId}`)
  }
  
}
