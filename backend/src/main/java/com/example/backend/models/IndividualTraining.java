package com.example.backend.models;

public class IndividualTraining {
    private int id;
    private int athleteId;
    private int trainerId;
    private int facilityId;
    private int sportId;
    private String scheduledAt;
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public IndividualTraining() {}

    public IndividualTraining(int id, int athleteId, int trainerId, int facilityId,
                               int sportId, String scheduledAt, String status) {
        this.id = id;
        this.athleteId = athleteId;
        this.trainerId = trainerId;
        this.facilityId = facilityId;
        this.sportId = sportId;
        this.scheduledAt = scheduledAt;
        this.status = status;
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

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    

}