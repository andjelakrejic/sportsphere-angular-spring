import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Message } from '../models/message';
import { Orders } from '../models/orders';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  
  private url = "http://localhost:8080/orders"
  private http = inject(HttpClient)

  getAllOrders(userId: number){
    return this.http.get<Orders[]>(`${this.url}/getAllOrders/${userId}`)
  }

  placeOrder(o: Orders){
    return this.http.post<Message>(`${this.url}/placeOrder`, o)
  }

  cancelActiveOrder(orderId: number){
  return this.http.post<Message>(`${this.url}/cancelActiveOrder/${orderId}`, {})
  }

  getAllOrdersForWorker() {
    return this.http.get<Orders[]>(`${this.url}/getAllOrdersForWorker`)
  }
  
  updateOrderStatus(orderId: number, status: string) {
    return this.http.put<Message>(`${this.url}/updateOrderStatus`, null, {
      params: { orderId, status }
    })
  }
}
