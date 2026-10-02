import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Worker } from '../models/worker';
import { Message } from '../models/message';
import { FacilityWorkerOption } from '../models/facilityWorkerOption';

@Injectable({ 
  providedIn: 'root' 
})

export class WorkerService {
  
  private http = inject(HttpClient);
  private url = "http://localhost:8080/workers";

  loginWorker(username: string, password: string){
      const data = {
        username: username,
        password: password,
      }
      return this.http.post<Worker>(`${this.url}/loginWorker`, data)
  }

  getAllWorkers(){
    return this.http.get<Worker[]>(`${this.url}/getAllWorkers`)
  }

  getWorker(id: number) {
    return this.http.get<Worker>(`${this.url}/getWorker/${id}`);
  }

  changePassword(username: string, oldPass: string, newPass: string){
   const data = {
     username: username, 
     oldPass: oldPass,
     newPass: newPass
   }
   return this.http.post<Message>(`${this.url}/changePassword`, data)
  }
  getFacilitiesAvailableForSecondWorker() {
    return this.http.get<FacilityWorkerOption[]>(`${this.url}/getFacilitiesAvailableForSecondWorker`);
  }

  registerWorker(w: Worker){
    return this.http.post<number>(`${this.url}/registerWorker`, w)
  }

  registerWorkerWithImage(formData: FormData) {
    return this.http.post(`${this.url}/registerWorker`, formData);
  }

  updateWorker(worker: Worker) {
    return this.http.post<Message>(`${this.url}/updateWorker`, worker);
  }

  uploadProfileImage(username: string, file: File) {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('image', file);
    return this.http.post(`${this.url}/uploadProfileImage`, formData, { responseType: 'text' });
  }

  getMyFacilities(workerId: number) {
    return this.http.get<any>(`${this.url}/getMyFacilities/${workerId}`);
  }
}