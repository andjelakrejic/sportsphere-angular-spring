package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Facility;
import com.example.backend.models.Sport;

public interface FacilityRepoInterface {
    public List<Facility> getActiveFacilities();
    public List<Facility> getTop3Facilities();
    // public int getNumOfLikes(int id);

    public List<String> getAllSports(); // vraca niz stringova naziva sporta
    public List<Sport> getAllSportsObject();
    public List<String> getActiveCities();
    
    public List<Facility> searchFacilities(String name, String city, String sport, String type);
    public List<Facility> searchFreeTodayFacilities(String name, String city, String sport, String type);

    public Facility getFacility(int id);
    public List<String> getFacilityImages(int id);
}
