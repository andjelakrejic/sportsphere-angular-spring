package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.Trainer;

public interface TrainerRepoInterface {
    
    public List<Trainer> getTrainersByFacilityAndSport(int facilityId, int sportId);
    public List<Trainer> getAllTrainers();
    public Message deactivateTrainer(int trainerId);
}
