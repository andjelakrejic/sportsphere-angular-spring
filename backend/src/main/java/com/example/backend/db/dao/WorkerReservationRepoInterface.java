package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.helpers.AthleteBlockStatusDTO;
import com.example.backend.models.helpers.WorkerReservationDTO;
import com.example.backend.models.helpers.WorkerTrainingDTO;

public interface WorkerReservationRepoInterface {
    
    List<WorkerReservationDTO> getReservationsForFacility(int facilityId);
    List<WorkerTrainingDTO> getTrainingsForFacility(int facilityId);
    Message confirmReservation(int reservationId);
    Message markNoShowReservation(int reservationId);
    Message confirmTraining(int trainingId);
    Message markNoShowTraining(int trainingId);
    
    AthleteBlockStatusDTO getBlockStatus(int athleteId, int facilityId);

}

