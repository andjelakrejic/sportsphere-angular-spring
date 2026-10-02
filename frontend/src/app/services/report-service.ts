import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private http = inject(HttpClient);
  private facilityUrl = 'http://localhost:8080/facilities';
  private orderUrl = 'http://localhost:8080/orders';

  downloadOccupancyReport(facilityId: number, month: string) {
    return this.http.get(`${this.facilityUrl}/reports/occupancy?facilityId=${facilityId}&month=${month}`, {
      responseType: 'blob'
    });
  }

  downloadEquipmentReport(month: string) {
    return this.http.get(`${this.orderUrl}/reports/equipment?month=${month}`, {
      responseType: 'blob'
    });
  }
}