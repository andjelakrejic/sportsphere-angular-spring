package com.example.backend.db.dao;

import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;

public interface AdminRepoInterface {
    public Admin loginAdmin(Admin a);

    public Message addSport(Sport s);

    public Message acceptRequest(); //zahtevi za registraciju od athlete ili worker
    public Message acceptFacilityRequest(); // odobrava novo unet sportski objekat od worker    

    public Message changeAthleteAccount();
    public Message changeWorkerAccount();
    public Message deleteAthleteAccount();
    public Message deleteWorkerAccount();
    // za pregled naloga imas getAthlete i getWorker iz userController



}
