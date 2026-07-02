package com.example.backend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ChartRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/chart")
@CrossOrigin(origins = "http://localhost:4200")
public class ChartController {

    @GetMapping("/countResPerSport/{athleteId}")
    public Map<String, Integer> countResPerSport(@PathVariable int athleteId) {
        return new ChartRepo().countResPerSport(athleteId);
    }

    @GetMapping("/countPlayedResPerSport/{athleteId}")
    public Map<String, Integer> countPlayedResPerSport(@PathVariable int athleteId) {
        return new ChartRepo().countPlayedResPerSport(athleteId);
    }

    @GetMapping("/reservationsPerMonth/{athleteId}")
    public Map<Integer, Integer> reservationsPerMonth(@PathVariable int athleteId) {
        return new ChartRepo().reservationsPerMonth(athleteId);
    }

    @GetMapping("/getTotalEquipmentSpending")
    public double getTotalEquipmentSpending() {
        return new ChartRepo().getTotalEquipmentSpending();
    }
}