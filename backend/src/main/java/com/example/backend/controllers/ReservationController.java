package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ReservationRepo;
import com.example.backend.models.Message;
import com.example.backend.models.Reservation;
import com.example.backend.models.helpers.CreateReservationObject;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;


@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    
    @PostMapping("/createReservation")
    public Message createReservation(@RequestBody CreateReservationObject obj){
        return new ReservationRepo().createReservation(obj);    
    }

    @GetMapping("/getReservations/{athleteId}")
    public List<Reservation> getReservations(@PathVariable int athleteId) {
        return new ReservationRepo().getReservations(athleteId);
    }

    @PostMapping("/cancelReservation")
    public Message cancelReservation(@RequestBody int id) {
        return new ReservationRepo().cancelReservation(id);
    }

    @PostMapping("/getReservationsForCourt")
    public List<Reservation> getReservationsForCourt(@RequestBody GetReservationObject obj) {
        return new ReservationRepo().getReservationsForCourt(obj);
    }

    @PostMapping("/updateReservationTime")
    public Message updateReservationTime(@RequestBody UpdateTimeObject obj) {
        return new ReservationRepo().updateReservationTime(obj);
    }
    
}
