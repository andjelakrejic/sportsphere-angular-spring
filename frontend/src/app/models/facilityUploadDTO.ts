export class CourtUploadDTO {
    name: string = ""
    type: string = ""
    capacity: number = 0
    equipmentDescription: string = ""
    sportId: number = 0
}

export class FacilityUploadDTO {
    name: string = ""
    city: string = ""
    address: string = ""
    description: string = ""
    workingHoursFrom: string = ""
    workingHoursTo: string = ""
    pricePerHour: number = 0
    maxNoShows: number = 0
    courts: CourtUploadDTO[] = []
    registrationNumber: number = 0
    taxId: number = 0
}