package com.example.backend.models.helpers;

import java.time.LocalDateTime;

public class CourtTrainingViewDTO {
    private int id;
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;
    private String status;

    public CourtTrainingViewDTO(int id, LocalDateTime timeFrom, LocalDateTime timeTo, String status) {
        this.id = id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.status = status;
    }

    public int getId() { return id; }
    public LocalDateTime getTimeFrom() { return timeFrom; }
    public LocalDateTime getTimeTo() { return timeTo; }
    public String getStatus() { return status; }
}
