package com.example.backend.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.WorkerReservationRepo;
import com.example.backend.models.Message;
import com.example.backend.models.helpers.WorkerReservationDTO;
import com.example.backend.models.helpers.WorkerTrainingDTO;

@RestController
@RequestMapping("/worker-reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkerReservationController {

    @GetMapping("/getReservations/{facilityId}")
    public List<WorkerReservationDTO> getReservations(@PathVariable int facilityId) {
        return new WorkerReservationRepo().getReservationsForFacility(facilityId);
    }

    @GetMapping("/getTrainings/{facilityId}")
    public List<WorkerTrainingDTO> getTrainings(@PathVariable int facilityId) {
        return new WorkerReservationRepo().getTrainingsForFacility(facilityId);
    }

    @PostMapping("/confirmReservation/{reservationId}")
    public Message confirmReservation(@PathVariable int reservationId) {
        return new WorkerReservationRepo().confirmReservation(reservationId);
    }

    @PostMapping("/noShowReservation/{reservationId}")
    public Message noShowReservation(@PathVariable int reservationId) {
        return new WorkerReservationRepo().markNoShowReservation(reservationId);
    }

    @PostMapping("/confirmTraining/{trainingId}")
    public Message confirmTraining(@PathVariable int trainingId) {
        return new WorkerReservationRepo().confirmTraining(trainingId);
    }

    @PostMapping("/noShowTraining/{trainingId}")
    public Message noShowTraining(@PathVariable int trainingId) {
        return new WorkerReservationRepo().markNoShowTraining(trainingId);
    }

}
