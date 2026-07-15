package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Equipment;
import com.example.backend.models.Message;

public interface EquipmentRepoInterface {

    public List<Equipment> getAllEquipment();
    public List<Equipment> getEquipmentForSport(int sportId); 
    public Message updateEquipment(Equipment e);  
    public Message addEquipment(Equipment e);
}
