package com.example.backend.controllers;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.db.dao.CourtRepo;
import com.example.backend.models.Court;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/courts")
@CrossOrigin(origins = "http://localhost:4200")
public class CourtController {
    
    @GetMapping("/getAvailableCourts/{facilityId}")
    public List<Court> getAvailableCourts(@PathVariable int facilityId) {
        return new CourtRepo().getAvailableCourts(facilityId);
    }

    @GetMapping("/getCourtsForFacility/{facilityId}")
    public List<Court> getCourtsForFacility(@PathVariable int facilityId) {
        return new CourtRepo().getCourtsForFacility(facilityId);
    }
}
