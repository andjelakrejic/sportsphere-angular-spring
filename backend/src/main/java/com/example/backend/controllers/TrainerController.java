package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.TrainerRepo;
import com.example.backend.models.Trainer;

@RestController
@RequestMapping("/trainers")
@CrossOrigin(origins = "http://localhost:4200")
public class TrainerController {

    @GetMapping("/getTrainersByFacilityAndSport")
    public List<Trainer> getTrainersByFacilityAndSport(@RequestParam int facilityId, @RequestParam int sportId) {
        return new TrainerRepo().getTrainersByFacilityAndSport(facilityId, sportId);
    }

    @GetMapping("/getAllTrainers")
    public List<Trainer> getAllTrainers() {
        return new TrainerRepo().getAllTrainers();
    }
}
