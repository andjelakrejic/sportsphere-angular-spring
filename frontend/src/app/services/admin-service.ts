import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Admin } from '../models/admin';
import { Message } from '../models/message';
import { Sport } from '../models/sport';
import { User } from '../models/user';
import { Facility } from '../models/facility';

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  private http = inject(HttpClient)

  private url = "http://localhost:8080/admin"
  

  loginAdmin(username: string, password: string){
    const data = {
      username: username,
      password: password,
    }
    return this.http.post<Admin>(`${this.url}/loginAdmin`, data)
  }

  addSport(s: Sport){
    return this.http.post<Message>(`${this.url}/addSport`, s)
  }

  acceptRequest(userId: number){
    return this.http.post<Message>(`${this.url}/acceptRequest`, userId)
  }

  denyRequest(userId: number){
    return this.http.post<Message>(`${this.url}/denyRequest`, userId)
  }

  updateUser(u: User) {
    return this.http.post<Message>(`${this.url}/updateUser`, u);
  }

  deleteUser(userId: number) {
    return this.http.post<Message>(`${this.url}/deleteUser`, userId);
  }  

  viewAllAccounts() {
    return this.http.get<User[]>(`${this.url}/viewAllAccounts`)
  }

  getPendingRequests() {
    return this.http.get<User[]>(`${this.url}/getPendingRequests`);
  }

  getPendingFacilities() {
    return this.http.get<Facility[]>(`${this.url}/getPendingFacilities`);
  }

  acceptFacilityRequest(facilityId: number) {
    return this.http.post<Message>(`${this.url}/acceptFacilityRequest`, facilityId);
  }

  denyFacilityRequest(facilityId: number) {
    return this.http.post<Message>(`${this.url}/denyFacilityRequest`, facilityId);
  }

  deactivateTrainer(trainerId: number) {
    return this.http.post<Message>(`${this.url}/deactivateTrainer`, trainerId);
  }

}
