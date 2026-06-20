package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Reservation;
import com.example.backend.models.Sport;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

public interface UserRepoInterface {

    public Athlete loginAthlete(Athlete a);
    public Worker loginWorker(Worker a);
    public int registerAthlete(Athlete a);
    public int registerWorker(Worker w);


    public Worker getWorker(String username);
    public Athlete getAthlete(String username);

    public Message updateAthlete(Athlete a);
    public Message updateFavoriteSports(FavoriteSportsObject obj);
    public Message changePassword(ChangePasswordObject obj);

    public List<Reservation> getReservations(int athleteId);
    public Message cancelReservation(int resId);

    public List<Sport> getAllSports();


}
