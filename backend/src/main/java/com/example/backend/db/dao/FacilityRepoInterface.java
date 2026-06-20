package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.CourtInfo;
import com.example.backend.models.Facility;

public interface FacilityRepoInterface {
    public List<Facility> getActiveFacilities();
    public List<Facility> getTop3Facilities();
    // public int getNumOfLikes(int id);
    public List<String> getAllSports();
    public List<String> getActiveCities();
    public List<Facility> searchFacilities(String name, String city, String sport, String type);
    public Facility getFacility(int id);
    public List<String> getFacilityImages(int id);
    public List<CourtInfo> getAvailableCourts(int id);

}
