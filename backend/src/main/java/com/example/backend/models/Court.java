package com.example.backend.models;

public class Court {
    private int id;
    private int facilityId;
    private String name;
    private String type;
    private int capacity;
    private String equipmentDescription;
    private int sportId; // konstruktor je bez njega


    public Court(int id, int facilityId, String name, String type, int capacity, String equipmentDescription) {
        this.id = id;
        this.facilityId = facilityId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.equipmentDescription = equipmentDescription;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public String getEquipmentDescription() {
        return equipmentDescription;
    }
    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }


    public int getSportId() {
        return sportId;
    }


    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    
}
