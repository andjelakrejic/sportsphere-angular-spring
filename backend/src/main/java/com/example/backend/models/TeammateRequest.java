package com.example.backend.models;

public class TeammateRequest {
    
    private int id;
    private int adId;
    private int athleteId;
    private String status;
    private String createdAt;
    
    
    public TeammateRequest(int id, int adId, int athleteId, String status, String createdAt) {
        this.id = id;
        this.adId = adId;
        this.athleteId = athleteId;
        this.status = status;
        this.createdAt = createdAt;
    }

     public TeammateRequest(int adId, int athleteId) { // za getTeammateRequest(adId, athleteId)
        this.id = 0;
        this.adId = adId;
        this.athleteId = athleteId;
        this.status = "";
        this.createdAt = "";
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAdId() {
        return adId;
    }
    public void setAdId(int adId) {
        this.adId = adId;
    }
    public int getAthleteId() {
        return athleteId;
    }
    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
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

    
}
