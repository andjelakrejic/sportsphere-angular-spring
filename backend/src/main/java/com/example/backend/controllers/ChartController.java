package com.example.backend.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ChartRepo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public Map<String, Integer> reservationsPerMonth(@PathVariable int athleteId) {
        return new ChartRepo().reservationsPerMonth(athleteId);
    }

    @PostMapping("/getTotalEquipmentSpending")
    public double getTotalEquipmentSpending(@RequestBody int id) {
        return new ChartRepo().getTotalEquipmentSpending(id);
    }
}