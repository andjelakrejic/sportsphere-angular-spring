package com.example.backend.models;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private String facilityName;
    private String city;
    private String courtName;
    private String sport;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private String status;
    
    public Reservation(int id, String facilityName, String city, String courtName, String sport, LocalDateTime timeFrom,
            LocalDateTime timeTo, String status) {
        this.id = id;
        this.facilityName = facilityName;
        this.city = city;
        this.courtName = courtName;
        this.sport = sport;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.status = status;
    }
    public Reservation() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public LocalDateTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalDateTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalDateTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalDateTime timeTo) {
        this.timeTo = timeTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

}
