import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Equipment } from '../models/equipment';
import { Sport } from '../models/sport';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {
  private url = "http://localhost:8080/equipment"
  private http = inject(HttpClient)

  getAllEquipment(){
    return this.http.get<Equipment[]>(`${this.url}/getAllEquipment`)
  }

  getEquipmentForSport(sportId: number){
    return this.http.get<Equipment[]>(`${this.url}/getEquipmentForSport/${sportId}`)
  }

  addEquipment(equipment: Equipment) {
    return this.http.post<Message>(`${this.url}/addEquipment`, equipment)
  }
  updateEquipment(equipment: Equipment) {
    return this.http.put<Message>(`${this.url}/updateEquipment`, equipment)
  }

  uploadEquipmentImage(file: File) {
    const formData = new FormData()
    formData.append('image', file)
    return this.http.post<string>(`${this.url}/uploadImage`, formData, { responseType: 'text' as 'json' })
  }
}
