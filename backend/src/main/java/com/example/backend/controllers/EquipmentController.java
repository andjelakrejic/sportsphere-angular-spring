package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.EquipmentRepo;
import com.example.backend.models.Equipment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/equipment")
@CrossOrigin(origins = "http://localhost:4200")
public class EquipmentController {
    
    @GetMapping("/getAllEquipment")
    public List<Equipment> getAllEquipment() {
        return new EquipmentRepo().getAllEquipment();
    }

    @GetMapping("/getEquipmentForSport/{sportId}")
    public List<Equipment> getEquipmentForSport(@PathVariable int sportId) {
        return new EquipmentRepo().getEquipmentForSport(sportId);
    }
    
}
