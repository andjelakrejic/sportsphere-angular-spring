package com.example.backend.db.dao;

import java.time.LocalDate;
import java.util.List;

import com.example.backend.models.Court;
import com.example.backend.models.Facility;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.helpers.CourtOccupancyDTO;
import com.example.backend.models.helpers.FacilityUploadDTO;

public interface FacilityRepoInterface {
    public List<Facility> getActiveFacilities();
    public List<Facility> getTop3Facilities();
    // public int getNumOfLikes(int id);

    public List<String> getAllSports(); // vraca niz stringova naziva sporta
    public List<Sport> getAllSportsObject();
    public List<Sport> getSportsForFacility(int id);
    public List<String> getActiveCities();
    
    public List<Facility> searchFacilities(String name, String city, String sport, String type);
    public List<Facility> searchFreeTodayFacilities(String name, String city, String sport, String type);

    public Facility getFacility(int id);
    public List<String> getFacilityImages(int id);

    // worker-facilities
    public Message addFacility(FacilityUploadDTO dto, int workerId);
    public Message updateFacility(Facility facility);
    public List<Facility> getFacilitiesByWorkerId(int workerId);
    public Message addCourt(Court court);
    public Message updateCourt(Court court);
    public List<Court> getCourtsByFacilityId(int facilityId);

    /// pdf generator
    public List<CourtOccupancyDTO> getFacilityOccupancy(int facilityId, LocalDate monthStart, LocalDate monthEnd);

    // admin
    public List<Facility> getPendingFacilities();
}
