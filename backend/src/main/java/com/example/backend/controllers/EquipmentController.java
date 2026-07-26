package com.example.backend.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.EquipmentRepo;
import com.example.backend.models.Equipment;
import com.example.backend.models.Message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/addEquipment")
    public Message addEquipment(@RequestBody Equipment e) {
        return new EquipmentRepo().addEquipment(e);
    }

    @PutMapping("/updateEquipment")
    public Message updateEquipment(@RequestBody Equipment e) {
        return new EquipmentRepo().updateEquipment(e);
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String filename = "equipment_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("images/" + filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok(filename);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed");
        }
    }
}
