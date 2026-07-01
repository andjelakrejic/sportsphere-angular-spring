package com.example.backend.models;

public class EquipmentOrders {
    private int id;
    private int orderId;
    private int equipmentId;
    private int quantity;
    private double priceAtPurchase;

    public EquipmentOrders() {}

    public EquipmentOrders(int id, int orderId, int equipmentId, int quantity, double priceAtPurchase) {
        this.id = id;
        this.orderId = orderId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getEquipmentId() {
        return equipmentId;
    }
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }
    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    
}
