package com.example.backend.models.helpers;

public class WorkerTrainingDTO {
    private int id;
    private String athleteUsername;
    private String trainerUsername;
    private String sportName;
    private String scheduledAt;
    private String status;
    private String trainingDate;
    private String timeFrom;
    private String timeTo;
    
    public WorkerTrainingDTO() {}
    
    public WorkerTrainingDTO(int id, String athleteUsername, String trainerUsername, String sportName, String trainingDate, String timeFrom, String timeTo,
            String scheduledAt, String status) {
        this.id = id;
        this.athleteUsername = athleteUsername;
        this.trainerUsername = trainerUsername;
        this.sportName = sportName;
        this.scheduledAt = scheduledAt;
        this.trainingDate = trainingDate;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.status = status;
    }

    public String getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(String trainingDate) {
        this.trainingDate = trainingDate;
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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAthleteUsername() {
        return athleteUsername;
    }
    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }
    public String getTrainerUsername() {
        return trainerUsername;
    }
    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
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
