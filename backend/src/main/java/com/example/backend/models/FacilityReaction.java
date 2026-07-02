package com.example.backend.models;

import java.time.LocalDateTime;

public class FacilityReaction {
    private int id;
    private int facilityId;
    private int athleteId;
    private String type; // LIKE, DISLIKE, COMMENT
    private String comment;
    private LocalDateTime createdAt;
    private String facilityName;

    
    public FacilityReaction() {}

    public FacilityReaction(int id, int facilityId, int athleteId, String type, String comment,
            LocalDateTime createdAt) {
        this.id = id;
        this.facilityId = facilityId;
        this.athleteId = athleteId;
        this.type = type;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }

     // transient - popunjava se samo pri prikazu, ne ide u bazu direktno
    private String athleteName;
    private boolean ownComment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAthleteName() { return athleteName; }
    public void setAthleteName(String athleteName) { this.athleteName = athleteName; }

    public boolean isOwnComment() { return ownComment; }
    public void setOwnComment(boolean ownComment) { this.ownComment = ownComment; }
    
}
