package com.example.backend.models.helpers;

public class BookTrainingRequest {
    private int athleteId;
    private int trainerId;
    private int facilityId;
    private int sportId;
    private String date;      // yyyy-MM-dd
    private String startTime; // HH:mm:ss
    private String endTime;   // HH:mm:ss

    public BookTrainingRequest() {}

    public int getAthleteId() { return athleteId; }
    public void setAthleteId(int athleteId) { this.athleteId = athleteId; }
    public int getTrainerId() { return trainerId; }
    public void setTrainerId(int trainerId) { this.trainerId = trainerId; }
    public int getFacilityId() { return facilityId; }
    public void setFacilityId(int facilityId) { this.facilityId = facilityId; }
    public int getSportId() { return sportId; }
    public void setSportId(int sportId) { this.sportId = sportId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}