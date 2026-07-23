package com.example.backend.models.helpers;

public class UpdateTimeObject {
    private int id;
    private String date;      // yyyy-MM-dd
    private String startTime; // HH:mm:ss
    private String endTime;   // HH:mm:ss

    public UpdateTimeObject() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}