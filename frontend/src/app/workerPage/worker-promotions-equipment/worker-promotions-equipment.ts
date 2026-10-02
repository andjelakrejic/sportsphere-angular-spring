import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Promotion } from '../../models/promotion';
import { Equipment } from '../../models/equipment';
import { Orders } from '../../models/orders';
import { Sport } from '../../models/sport';
import { Facility } from '../../models/facility';
import { WorkerNavBar } from '../worker-nav-bar/worker-nav-bar';
import { PromotionService } from '../../services/promotion-service';
import { EquipmentService } from '../../services/equipment-service';
import { OrderService } from '../../services/order-service';
import { WorkerService } from '../../services/worker-service';
import { FacilityService } from '../../services/facility-service';
import { Worker } from '../../models/worker';

@Component({
  selector: 'app-worker-promotions-equipment',
  standalone: true,
  imports: [FormsModule, WorkerNavBar],
  templateUrl: './worker-promotions-equipment.html',
  styleUrl: './worker-promotions-equipment.css'
})
export class WorkerPromotionsEquipment implements OnInit {

  private promotionService = inject(PromotionService)
  private equipmentService = inject(EquipmentService)
  private orderService = inject(OrderService)
  private workerService = inject(WorkerService)
  private facilityService = inject(FacilityService)

  activeTab: 'promotions' | 'equipment' | 'orders' = 'promotions'

  facilities: Facility[] = []
  selectedFacilityId: number = 0
  promotionSports: Sport[] = []

  promotions: Promotion[] = []
  promotionForm: Promotion = new Promotion()
  editingPromotionId: number | null = null

  equipmentSelectedFile: File | null = null
  equipmentPreviewUrl: string | null = null
  equipmentSports: Sport[] = []
  equipment: Equipment[] = []
  equipmentForm: Equipment = new Equipment()
  editingEquipmentId: number | null = null

  orders: Orders[] = []

  worker: Worker = new Worker()

  successMsg = ""
  errorMsg = ""

  ngOnInit(): void {
    this.worker = JSON.parse(localStorage.getItem('worker')!)

    this.facilityService.getFacilitiesByWorker(this.worker.id).subscribe(facilities => {
      this.facilities = facilities
      if (this.facilities.length > 0) {
        this.selectedFacilityId = this.facilities[0].id
        this.onFacilityChange()
      }
    })

    this.facilityService.getAllSportsObject().subscribe(s => this.equipmentSports = s)

    this.loadEquipment()
    this.loadOrders()
  }

  onFacilityChange() {
    this.successMsg = ""
    this.errorMsg = ""
    this.loadPromotionSports()
    this.loadPromotions()
    this.resetPromotionForm()
  }

  loadPromotionSports() {
    if (!this.selectedFacilityId) return
    this.facilityService.getSportsForFacility(this.selectedFacilityId).subscribe(s => this.promotionSports = s)
  }

  setTab(tab: 'promotions' | 'equipment' | 'orders') {
    this.activeTab = tab
    this.successMsg = ""
    this.errorMsg = ""
  }

  // ---------- PROMOTIONS ----------

  loadPromotions() {
    if (!this.selectedFacilityId) return
    this.promotionService.getPromotionsByFacility(this.selectedFacilityId).subscribe(p => this.promotions = p)
  }

  resetPromotionForm() {
    this.promotionForm = new Promotion()
    this.promotionForm.facilityId = this.selectedFacilityId
    this.editingPromotionId = null
  }

  editPromotion(p: Promotion) {
    this.promotionForm = { ...p }
    this.editingPromotionId = p.id
  }

  savePromotion() {
    this.successMsg = ""
    this.errorMsg = ""
    this.promotionForm.facilityId = this.selectedFacilityId

    const obs = this.editingPromotionId
      ? this.promotionService.updatePromotion(this.promotionForm)
      : this.promotionService.addPromotion(this.promotionForm)

    obs.subscribe(res => {
      if (res.success) {
        this.successMsg = res.message
        this.loadPromotions()
        this.resetPromotionForm()
      } else {
        this.errorMsg = res.message
      }
    })
  }

  isPromotionActive(p: Promotion): boolean {
    const today = new Date().toISOString().slice(0, 10)
    return p.dateFrom <= today && p.dateTo >= today
  }

  // ---------- EQUIPMENT ----------

  getImageUrl(filename: string): string {
    return `http://localhost:8080/images/${filename}`
  }

  onEquipmentImageSelected(event: Event) {
    const input = event.target as HTMLInputElement
    if (!input.files || input.files.length === 0) return

    const file = input.files[0]

    if (!file.type.startsWith('image/')) {
      this.errorMsg = 'Please select a valid image file.'
      return
    }

    // preview
    const reader = new FileReader()
    reader.onload = (e) => this.equipmentPreviewUrl = e.target?.result as string
    reader.readAsDataURL(file)

    // upload odmah i sačuvaj filename
    this.equipmentService.uploadEquipmentImage(file).subscribe(filename => {
      this.equipmentForm.imageUrl = filename  // ← sačuvaj za kasniji save
    })
  }

  loadEquipment() {
    this.equipmentService.getAllEquipment().subscribe(e => this.equipment = e)
  }

  resetEquipmentForm() {
    this.equipmentForm = new Equipment()
    this.editingEquipmentId = null
  }

  editEquipment(e: Equipment) {
    this.equipmentForm = { ...e }
    this.editingEquipmentId = e.id
  }

  saveEquipment() {
    this.successMsg = ""
    this.errorMsg = ""

    const obs = this.editingEquipmentId
      ? this.equipmentService.updateEquipment(this.equipmentForm)
      : this.equipmentService.addEquipment(this.equipmentForm)

    obs.subscribe(res => {
      if (res.success) {
        this.successMsg = res.message
        this.loadEquipment()
        this.resetEquipmentForm()
        this.equipmentSelectedFile = null
        this.equipmentPreviewUrl = null
      } else {
        this.errorMsg = res.message
      }
    })
  }

  // ---------- ORDERS ----------

  loadOrders() {
    this.orderService.getAllOrdersForWorker().subscribe(o => this.orders = o)
  }

  getEquipmentName(equipmentId: number): string {
    return this.equipment.find(e => e.id === equipmentId)?.name ?? '—'
  }

  updateStatus(order: Orders, status: string) {
    this.orderService.updateOrderStatus(order.id, status).subscribe(res => {
      if (res.success) {
        order.status = status
        this.successMsg = res.message
      } else {
        this.errorMsg = res.message
      }
    })
  }
}