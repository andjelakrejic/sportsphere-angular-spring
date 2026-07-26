package com.example.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private int id;
    private int athleteId;
    private double totalPrice;
    private String status; // ORDERED, ACCEPTED, PICKED UP, CANCELED, 
    private String createdAt;
    private List<EquipmentOrders> items;
    
    public Orders() {}

    public Orders(int id, int athleteId, double totalPrice, String status, String createdAt) {
        this.id = id;
        this.athleteId = athleteId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<EquipmentOrders> getItems() {
        return items;
    }

    public void setItems(List<EquipmentOrders> items) {
        this.items = items;
    }
    
    


    
}
