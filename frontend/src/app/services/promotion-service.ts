import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Promotion } from '../models/promotion';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class PromotionService {
  
  private http = inject(HttpClient)
  private url = "http://localhost:8080/promotions"

  getActivePromotions(){
    return this.http.get<Promotion[]>(`${this.url}/getActivePromotions`)
  }

  getPromotionsByFacility(facilityId: number) {
    return this.http.get<Promotion[]>(`${this.url}/getPromotionsByFacility/${facilityId}`)
  }

  addPromotion(promotion: Promotion) {
    return this.http.post<Message>(`${this.url}/addPromotion`, promotion)
  }

  updatePromotion(promotion: Promotion) {
    return this.http.put<Message>(`${this.url}/updatePromotion`, promotion)
  }
}
