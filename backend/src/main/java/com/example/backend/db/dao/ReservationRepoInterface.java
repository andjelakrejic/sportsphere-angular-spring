package com.example.backend.db.dao;
import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.Reservation;
import com.example.backend.models.helpers.CreateReservationObject;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

public interface ReservationRepoInterface {

    public Message createReservation(CreateReservationObject obj);

    public List<Reservation> getReservations(int athleteId);
    public Message cancelReservation(int resId);

    public List<Reservation> getReservationsForCourt(GetReservationObject obj);
    Message updateReservationTime(UpdateTimeObject obj);

}