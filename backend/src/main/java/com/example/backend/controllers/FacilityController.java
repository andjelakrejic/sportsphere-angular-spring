package com.example.backend.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.FacilityRepo;
import com.example.backend.models.Facility;
import com.example.backend.models.Sport;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/facilities")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityController {

    @GetMapping("/getActiveFacilities")
    public List<Facility> getActiveFacilities() {
        return new FacilityRepo().getActiveFacilities();
    }

    @GetMapping("/getTop3Facilities")
    public List<Facility> getTop3Facilities() {
        return new FacilityRepo().getTop3Facilities();
    }
    
    // @GetMapping("/getNumOfLikes/{facilityId}")
    // public int getTop3Facilities(@PathVariable int facilityId) {
    //     return new FacilityRepo().getNumOfLikes(facilityId);
    // }

    @GetMapping("/getActiveCities")
    public List<String> getActiveCities() {
       return new FacilityRepo().getActiveCities();
    }

    @GetMapping("/getAllSports")
    public List<String> getAllSports() {
       return new FacilityRepo().getAllSports();
    }

    @GetMapping("/getAllSportsObject")
    public List<Sport> getAllSportsObject() {
       return new FacilityRepo().getAllSportsObject();
    }

    @GetMapping("/searchFacility")
    public List<Facility> searchFacilities(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String sport,
        @RequestParam(required = false) String type
    ) {
        return new FacilityRepo().searchFacilities(name, city, sport, type);
    }

    @GetMapping("/searchFreeTodayFacility")
    public List<Facility> searchFreeTodayFacilities(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String sport,
        @RequestParam(required = false) String type
    ) {
        return new FacilityRepo().searchFreeTodayFacilities(name, city, sport, type);
    }

    @GetMapping("/getFacility/{facilityId}")
    public Facility getFacility(@PathVariable int facilityId) {
        return new FacilityRepo().getFacility(facilityId);
    }

    @GetMapping("/getFacilityImages/{facilityId}")
    public List<String> getFacilityImages(@PathVariable int facilityId) {
        return new FacilityRepo().getFacilityImages(facilityId);
    }
    
    
}
