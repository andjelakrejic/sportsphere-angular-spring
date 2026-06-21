package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Court;

public interface CourtRepoInterface {

    public List<Court> getAvailableCourts(int id);
    
    public List<Court> getCourtsForFacility(int facilityId);

    
}