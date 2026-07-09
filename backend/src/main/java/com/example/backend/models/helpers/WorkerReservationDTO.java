package com.example.backend.models.helpers;

public class WorkerReservationDTO {
    private int id;
    private String courtName;
    private String athleteUsername;
    private String sportName;
    private String date;
    private String timeFrom;
    private String timeTo;
    private String status;
    
    public WorkerReservationDTO() {}
    
    public WorkerReservationDTO(int id, String courtName, String athleteUsername, String sportName, String date,
            String timeFrom, String timeTo, String status) {
        this.id = id;
        this.courtName = courtName;
        this.athleteUsername = athleteUsername;
        this.sportName = sportName;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCourtName() {
        return courtName;
    }
    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }
    public String getAthleteUsername() {
        return athleteUsername;
    }
    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTimeFrom() {
        return timeFrom;
    }
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }
    public String getTimeTo() {
        return timeTo;
    }
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    
}
