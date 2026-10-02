import { EquipmentOrders } from "./equipmentOrders"

export class Orders {
    id: number = 0
    createdAt: string = ""
    totalPrice: number = 0.0
    athleteId: number = 0
    status: string = "" // ORDERED, ACCEPTED, PICKED UP, CANCELLED
    items: EquipmentOrders[] = []
}