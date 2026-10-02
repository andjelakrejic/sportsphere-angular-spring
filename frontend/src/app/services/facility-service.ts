import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Facility } from '../models/facility';
import { Sport } from '../models/sport';
import { Message } from '../models/message';
import { Court } from '../models/court';
import { FacilityUploadDTO } from '../models/facilityUploadDTO';

@Injectable({
  providedIn: 'root',
})
export class FacilityService {

  private http = inject(HttpClient)
  private router = inject(Router)
  private url = "http://localhost:8080/facilities"

  getActiveFacilities(){
    return this.http.get<Facility[]>(`${this.url}/getActiveFacilities`)
  }

  getTop3Facilities(){
    return this.http.get<Facility[]>(`${this.url}/getTop3Facilities`)
  }
  
  getActiveCities() {
  return this.http.get<string[]>(`${this.url}/getActiveCities`);
}

  getAllSports() {
    return this.http.get<string[]>(`${this.url}/getAllSports`);
  }

  getAllSportsObject(){
    return this.http.get<Sport[]>(`${this.url}/getAllSportsObject`);
  }

  getSportsForFacility(id: number){
    return this.http.get<Sport[]>(`${this.url}/getSportsForFacility/${id}`);
  }

  searchFacilities(name: string, city: string, sport: string, type: string) {
    return this.http.get<Facility[]>(`${this.url}/searchFacility`, {
      params: { name, city, sport, type }
    });
  }

  searchFreeTodayFacilites(name: string, city: string, sport: string, type: string) {
    return this.http.get<Facility[]>(`${this.url}/searchFreeTodayFacility`, {
      params: { name, city, sport, type }
    });
  }

  getFacility(id: number){
    return this.http.get<Facility>(`${this.url}/getFacility/${id}`)
  }

  getFacilityImages(facilityId: number) {
    return this.http.get<string[]>(`${this.url}/getFacilityImages/${facilityId}`);
  }

  // worker-facilities
  addFacility(dto: FacilityUploadDTO, workerId: number) {
    return this.http.post<Message>(`${this.url}/addFacility?workerId=${workerId}`, dto);
  }

  uploadFacilityJson(file: File, workerId: number) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Message>(`${this.url}/uploadFacilityJson?workerId=${workerId}`, formData);
  }

  updateFacility(facility: Facility) {
    return this.http.put<Message>(`${this.url}/updateFacility`, facility);
  }

  getFacilitiesByWorker(workerId: number) {
    return this.http.get<Facility[]>(`${this.url}/getFacilitiesByWorker/${workerId}`);
  }

  getCourtsByFacility(facilityId: number) {
    return this.http.get<Court[]>(`${this.url}/getCourtsByFacility/${facilityId}`);
  }

  addCourt(court: Court) {
    return this.http.post<Message>(`${this.url}/addCourt`, court);
  }

  updateCourt(court: Court) {
    return this.http.put<Message>(`${this.url}/updateCourt`, court);
  }
}
