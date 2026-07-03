package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.IndividualTraining;

public interface IndividualTrainingRepoInterface {
    public boolean bookTraining(int athleteId, int trainerId, int facilityId, int sportId, String scheduledAt);
    public List<IndividualTraining> getAthleteTrainings(int athleteId);
}
