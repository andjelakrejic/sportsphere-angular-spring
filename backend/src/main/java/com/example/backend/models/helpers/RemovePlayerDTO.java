package com.example.backend.models.helpers;

public class RemovePlayerDTO {
    private int adId;
    private int athleteId;

    private RemovePlayerDTO(int adId, int athlete_id){
        this.adId = adId;
        this.athleteId = athlete_id;
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

    
}
