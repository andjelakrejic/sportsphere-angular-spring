package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Equipment;

public interface EquipmentRepoInterface {

    public List<Equipment> getAllEquipment();
    public List<Equipment> getEquipmentForSport(int sportId);   
    
}
