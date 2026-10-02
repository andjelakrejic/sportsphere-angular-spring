import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ChartService {
  private http = inject(HttpClient)
  private url = "http://localhost:8080/chart"

  countResPerSport(athleteId: number) {
    return this.http.get<Record<string, number>>(`${this.url}/countResPerSport/${athleteId}`)
  }

  countPlayedResPerSport(athleteId: number) {
    return this.http.get<Record<string, number>>(`${this.url}/countPlayedResPerSport/${athleteId}`)
  }

  reservationsPerMonth(athleteId: number) {
    return this.http.get<{[key: string]: number}>(`${this.url}/reservationsPerMonth/${athleteId}`)
  }

  getTotalEquipmentSpending(id: number) {
    return this.http.post<number>(`${this.url}/getTotalEquipmentSpending`, id)
  }
}