import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Reservation } from '../models/reservation';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private http = inject(HttpClient)
  private url = "http://localhost:8080/reservations"
  
  getReservations(id: number){
    return this.http.get<Reservation[]>(`${this.url}/getReservations/${id}`)
  }
  
  cancelReservation(id: number){
    return this.http.post<Message>(`${this.url}/cancelReservation`, id) 
  }

  getReservationsForCourt(obj: any) {
    return this.http.post<Reservation[]>(`${this.url}/getReservationsForCourt`, obj)
  }

  createReservation(obj: any) {
    return this.http.post<any>(`${this.url}/createReservation`, obj);
  }

  updateReservationTime(obj: any) {
    return this.http.post<Message>(`${this.url}/updateReservationTime`, obj);
  }
}
