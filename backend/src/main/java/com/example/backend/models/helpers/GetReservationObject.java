package com.example.backend.models.helpers;

import java.time.LocalDate;

public class GetReservationObject {
    int courtId;
    LocalDate weekStart;
    LocalDate weekEnd;

    

    public GetReservationObject(int courtId, LocalDate weekStart, LocalDate weekEnd) {
        this.courtId = courtId;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
    }

    public GetReservationObject(){}

    
    public int getCourtId() {
        return courtId;
    }
    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }
    public LocalDate getWeekStart() {
        return weekStart;
    }
    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }
    public LocalDate getWeekEnd() {
        return weekEnd;
    }
    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    
}
