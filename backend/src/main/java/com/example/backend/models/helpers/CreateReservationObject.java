package com.example.backend.models.helpers;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.backend.models.Sport;

public class CreateReservationObject {
    int courtId;
    int athleteId;
    int sportId;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    Sport sport;
    
    public CreateReservationObject(int courtId, int athleteId, LocalDate date, LocalTime startTime, LocalTime endTime,
            Sport sport, int sportId) {
        this.courtId = courtId;
        this.athleteId = athleteId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sport = sport;
        this.sportId = sportId;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    
    
}
