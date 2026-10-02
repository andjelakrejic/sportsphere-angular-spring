import { Component, inject } from '@angular/core';
import { AthleteNavBar } from '../athlete-nav-bar/athlete-nav-bar';
import { EquipmentService } from '../../services/equipment-service';
import { OrderService } from '../../services/order-service';
import { FacilityService } from '../../services/facility-service';
import { Equipment } from '../../models/equipment';
import { Sport } from '../../models/sport';
import { Orders } from '../../models/orders';
import { EquipmentOrders } from '../../models/equipmentOrders';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-athlete-shop',
  imports: [AthleteNavBar, FormsModule, CommonModule],
  templateUrl: './athlete-shop.html',
  styleUrl: './athlete-shop.css',
})
export class AthleteShop {
  equipmentService = inject(EquipmentService)
  orderService = inject(OrderService)
  facilityService = inject(FacilityService)

  allEquipment: Equipment[] = []
  filteredEquipment: Equipment[] = []
  sports: Sport[] = []
  cart: { equipment: Equipment, quantity: number }[] = []
  orders: Orders[] = []
  activeOrders: Orders[] = []
  historyOrders: Orders[] = []

  selectedSportId: number = 0
  athleteId: number = 0
  view: string = 'shop' // 'shop' | 'cart' | 'orders'

  ngOnInit() {
    const athlete = JSON.parse(localStorage.getItem('athlete') || '{}')
    this.athleteId = athlete.id

    this.loadEquipment()
    this.loadSports()
    this.loadOrders()
  }

  loadEquipment() {
    this.equipmentService.getAllEquipment().subscribe(data => {
      this.allEquipment = data
      this.filteredEquipment = data
    })
  }

  loadSports() {
    this.facilityService.getAllSportsObject().subscribe(data => {
      this.sports = data
    })
  }

  loadOrders() {
    this.orderService.getAllOrders(this.athleteId).subscribe(data => {
      this.orders = data
      this.activeOrders = data.filter(o => o.status === 'ORDERED')
      this.historyOrders = data.filter(o => o.status !== 'ORDERED')
    })
  }

  filterBySport(sportId: number) {
    this.selectedSportId = sportId
    if (sportId === 0) {
      this.filteredEquipment = this.allEquipment
    } else {
      this.equipmentService.getEquipmentForSport(sportId).subscribe(data => {
        this.filteredEquipment = data
      })
    }
  }

  addToCart(equipment: Equipment) {
    const existing = this.cart.find(c => c.equipment.id === equipment.id)
    if (existing) {
      if (existing.quantity < equipment.stockQuantity) {
        existing.quantity++
      }
    } else {
      this.cart.push({ equipment, quantity: 1 })
    }
  }

  removeFromCart(equipmentId: number) {
    this.cart = this.cart.filter(c => c.equipment.id !== equipmentId)
  }

  changeQuantity(equipmentId: number, delta: number) {
    const item = this.cart.find(c => c.equipment.id === equipmentId)
    if (!item) return
    const newQty = item.quantity + delta
    if (newQty < 1) {
      this.removeFromCart(equipmentId)
    } else if (newQty <= item.equipment.stockQuantity) {
      item.quantity = newQty
    }
  }

  getTotalPrice(): number {
    return this.cart.reduce((sum, c) => sum + c.equipment.price * c.quantity, 0)
  }

  placeOrder() {
    if (this.cart.length === 0) return

    const items: EquipmentOrders[] = this.cart.map(c => {
      const item = new EquipmentOrders()
      item.equipmentId = c.equipment.id
      item.quantity = c.quantity
      item.priceAtPurchase = c.equipment.price
      return item
    })

    const order = new Orders()
    order.athleteId = this.athleteId
    order.totalPrice = this.getTotalPrice()
    order.status = 'ORDERED'
    order.items = items

    this.orderService.placeOrder(order).subscribe(res => {
      alert(res.message)
      if (res.message === 'Order placed') {
        this.cart = []
        this.view = 'orders'
        this.loadOrders()
        this.loadEquipment()
      }
    })
  }

  cancelOrder(orderId: number) {
    this.orderService.cancelActiveOrder(orderId).subscribe(res => {
      alert(res.message)
      this.loadOrders()
    })
  }

  getCartCount(): number {
    return this.cart.reduce((sum, c) => sum + c.quantity, 0)
  }

  getImageUrl(filename: string): string {
    return `http://localhost:8080/images/${filename}`
  }
} 
