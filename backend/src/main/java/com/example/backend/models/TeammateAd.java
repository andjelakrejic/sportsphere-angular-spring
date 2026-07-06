package com.example.backend.models;

public class TeammateAd {
    private int id;
    private int athleteId;
    private int sportId;
    private String city;
    private String date;
    private String timeSlot;
    private int totalPlayersNeeded;
    private int missingPlayers;
    private String status;
    private String createdAt;    
    
    public TeammateAd(int id, int athleteId, int sportId, String city, String date, String timeSlot,
            int totalPlayersNeeded, int missingPlayers, String status, String createdAt) {
        this.id = id;
        this.athleteId = athleteId;
        this.sportId = sportId;
        this.city = city;
        this.date = date;
        this.timeSlot = timeSlot;
        this.totalPlayersNeeded = totalPlayersNeeded;
        this.missingPlayers = missingPlayers;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TeammateAd(int athleteId, int sportId, String city, String date, String timeSlot,
            int totalPlayersNeeded, int missingPlayers, String status, String createdAt) {
        this.athleteId = athleteId;
        this.sportId = sportId;
        this.city = city;
        this.date = date;
        this.timeSlot = timeSlot;
        this.totalPlayersNeeded = totalPlayersNeeded;
        this.missingPlayers = missingPlayers;
        this.status = status;
        this.createdAt = createdAt;
    }

    public TeammateAd() {}

    
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
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTimeSlot() {
        return timeSlot;
    }
    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
    public int getTotalPlayersNeeded() {
        return totalPlayersNeeded;
    }
    public void setTotalPlayersNeeded(int totalPlayersNeeded) {
        this.totalPlayersNeeded = totalPlayersNeeded;
    }
    public int getMissingPlayers() {
        return missingPlayers;
    }
    public void setMissingPlayers(int missingPlayers) {
        this.missingPlayers = missingPlayers;
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

