import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Athlete } from '../models/athlete';
import { Admin } from '../models/admin';
import { Message } from '../models/message';
import { Worker } from '../models/worker';
import { Sport } from '../models/sport';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private http = inject(HttpClient)
  private url = "http://localhost:8080/users"

  loginAthlete(username: string, password: string){
    const data = {
      username: username,
      password: password,
    }
    return this.http.post<Athlete>(`${this.url}/loginAthlete`, data)
  }

  getAllAthletes(){
    return this.http.get<Athlete[]>(`${this.url}/getAllAthletes`)
  }

  // moze samo 1 admin po sistemu - ovo mzd ne treba
  getAdmin(){
    return this.http.get<Admin>(`${this.url}/getAdmin`)
  }

  getAthlete(username: string){
    return this.http.get<Athlete>(`${this.url}/getAthlete/${username}`)
  }

  getAthleteById(id: number){
    return this.http.get<Athlete>(`${this.url}/getAthleteById/${id}`)
  }

  updateAthlete(a: Athlete){
    return this.http.post<Message>(`${this.url}/updateAthlete`, a)
  }

  updateFavoriteSports(data: { athleteId: number, sportIds: number[] }){
    return this.http.post<Message>(`${this.url}/updateFavoriteSports`, data)
  }

  changePassword(username: string, oldPass: string, newPass: string){
    const data = {
      username: username,
      oldPass: oldPass,
      newPass: newPass
    }
    return this.http.post<Message>(`${this.url}/changePassword`, data)
  }

  uploadProfileImage(username: string, file: File){
    const formData = new FormData();
    formData.append('username', username);
    formData.append('image', file);
    return this.http.post(`${this.url}/uploadProfileImage`, formData, { responseType: 'text' });
  }

  // Register

  registerAthlete(a: Athlete){
    return this.http.post<number>(`${this.url}/registerAthlete`, a)
  }

  registerAthleteWithImage(formData: FormData) {
    return this.http.post(`${this.url}/registerAthlete`, formData);
  }

  getFavoriteSports(id: number){
    return this.http.get<Sport[]>(`${this.url}/getFavoriteSports/${id}`)
  }

}
