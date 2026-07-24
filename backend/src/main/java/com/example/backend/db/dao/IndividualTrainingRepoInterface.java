package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.IndividualTraining;
import com.example.backend.models.Message;
import com.example.backend.models.helpers.BookTrainingRequest;
import com.example.backend.models.helpers.CourtTrainingViewDTO;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

public interface IndividualTrainingRepoInterface {
    public List<IndividualTraining> getAthleteTrainings(int athleteId);

    List<CourtTrainingViewDTO> getTrainingsForCourt(GetReservationObject obj);
    Message updateTrainingTime(UpdateTimeObject obj);

    Message bookTraining(BookTrainingRequest req);
    
}
