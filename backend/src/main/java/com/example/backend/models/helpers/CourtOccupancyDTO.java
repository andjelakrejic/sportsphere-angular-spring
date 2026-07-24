package com.example.backend.models.helpers;

public class CourtOccupancyDTO {
    private String courtName;
    private String courtType;
    private double bookedHours;
    private double availableHours;
    private double occupancyPercent;

    public CourtOccupancyDTO(String courtName, String courtType, double bookedHours, double availableHours) {
        this.courtName = courtName;
        this.courtType = courtType;
        this.bookedHours = bookedHours;
        this.availableHours = availableHours;
        this.occupancyPercent = availableHours > 0 ? (bookedHours / availableHours) * 100 : 0;
    }

    public String getCourtName() { return courtName; }
    public String getCourtType() { return courtType; }
    public double getBookedHours() { return bookedHours; }
    public double getAvailableHours() { return availableHours; }
    public double getOccupancyPercent() { return occupancyPercent; }
}