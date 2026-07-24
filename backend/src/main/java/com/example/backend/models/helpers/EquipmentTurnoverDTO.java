package com.example.backend.models.helpers;

public class EquipmentTurnoverDTO {
    private String equipmentName;
    private int quantitySold;
    private double revenue;

    public EquipmentTurnoverDTO(String equipmentName, int quantitySold, double revenue) {
        this.equipmentName = equipmentName;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public String getEquipmentName() { return equipmentName; }
    public int getQuantitySold() { return quantitySold; }
    public double getRevenue() { return revenue; }
}