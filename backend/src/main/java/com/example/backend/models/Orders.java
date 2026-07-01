package com.example.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private int id;
    private int athleteId;
    private double price;
    private String status; // ORDERED, PICKED UP, CANCELED
    private String createdAt;
    private List<EquipmentOrders> items;
    
    public Orders() {}

    public Orders(int id, int athleteId, double price, String status, String createdAt) {
        this.id = id;
        this.athleteId = athleteId;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
