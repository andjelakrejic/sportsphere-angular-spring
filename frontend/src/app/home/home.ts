import { Component, inject, OnInit } from '@angular/core';
import { Search } from '../search/search';
import { Header } from '../header/header';
import { Facility } from '../models/facility';
import { FacilityService } from '../services/facility-service';
import { Promotion } from '../models/promotion';
import { PromotionService } from '../services/promotion-service';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [Search, CommonModule, Header, DatePipe],
  templateUrl: './home.html',
  styleUrl: './home.css',
})

export class Home implements OnInit{
  title = "SportSphere Hub"
  message = ""

  activeFacilities: Facility[] = []
  top3Facilities: Facility[] = []
  numActiveFacilities = 0
  numActivePromotions = 0

  activePromotions: Promotion[] = []

  private facilityService = inject(FacilityService)
  private promotionService = inject(PromotionService)
  

  ngOnInit(): void {
    this.facilityService.getActiveFacilities().subscribe(data => {
      if(data == null){
        this.message = "Error getting active facilities (or no active facilities)"
        this.numActiveFacilities = 0
      } else {
        this.activeFacilities = data
        this.numActiveFacilities = this.activeFacilities.length
      }
    })

    this.facilityService.getTop3Facilities().subscribe(data => {
      if(data == null){
        this.message = "Error getting top 3 facilities"
      } else {
        this.top3Facilities = data
      }
    })

    this.promotionService.getActivePromotions().subscribe(data => {
      if(data == null){
        this.message = "Error getting active promotions (or no active facilities)"
        this.numActivePromotions = 0
      } else {
        this.activePromotions = data
        this.numActivePromotions = this.activeFacilities.length
      }
    })

  }

}
