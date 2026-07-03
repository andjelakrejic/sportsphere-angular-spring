package com.example.backend.db.dao;

import java.util.List;
import com.example.backend.models.Trainer;

public interface TrainerRepoInterface {
    
    public List<Trainer> getTrainersByFacilityAndSport(int facilityId, int sportId);
    
}
