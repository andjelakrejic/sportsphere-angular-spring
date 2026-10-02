import { User } from "./user"

export class Worker extends User {
    nameOfPlace = ""
    address = ""
    JMBG = ""
    PIB = ""
    facilityId: number | null = null
}