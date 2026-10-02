import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Equipment } from '../models/equipment';

@Injectable({
  providedIn: 'root'
})
export class ShopService {
  private http = inject(HttpClient);
  private url = "http://localhost:8080/shop"; // Tvoj backend URL za prodavnicu

  // 1. Povuci svu opremu za katalog
  getAllEquipment() {
    return this.http.get<Equipment[]>(`${this.url}/getAllEquipment`);
  }

  // 2. Pošalji novu porudžbinu na backend
  createOrder(orderData: any) {
    return this.http.post(`${this.url}/createOrder`, orderData);
  }
}
