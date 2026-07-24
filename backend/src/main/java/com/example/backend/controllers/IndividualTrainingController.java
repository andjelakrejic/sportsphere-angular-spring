package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.IndividualTrainingRepo;
import com.example.backend.models.IndividualTraining;
import com.example.backend.models.Message;
import com.example.backend.models.helpers.BookTrainingRequest;
import com.example.backend.models.helpers.CourtTrainingViewDTO;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

@RestController
@RequestMapping("/individual-trainings")
@CrossOrigin(origins = "http://localhost:4200")
public class IndividualTrainingController {


    @PostMapping("/bookTraining")
    public Message bookTraining(@RequestBody BookTrainingRequest request) {
        return new IndividualTrainingRepo().bookTraining(request);
    }

    @GetMapping("/athlete/{athleteId}")
    public List<IndividualTraining> getAthleteTrainings(@PathVariable int athleteId) {
        return new IndividualTrainingRepo().getAthleteTrainings(athleteId);
    }

    @PostMapping("/getTrainingsForCourt")
    public List<CourtTrainingViewDTO> getTrainingsForCourt(@RequestBody GetReservationObject obj) {
        return new IndividualTrainingRepo().getTrainingsForCourt(obj);
    }

    @PostMapping("/updateTrainingTime")
    public Message updateTrainingTime(@RequestBody UpdateTimeObject obj) {
        return new IndividualTrainingRepo().updateTrainingTime(obj);
    }

}
