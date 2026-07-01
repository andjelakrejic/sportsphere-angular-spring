package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Admin;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.User;

public interface AdminRepoInterface {
    public Admin loginAdmin(Admin a);

    public Message addSport(Sport s);

    public Message acceptRequest(int userId); //zahtevi za registraciju se posmatraju u "posebnom pregledu"
    public Message denyRequest(int userId);
    public List<User> viewAllAccounts();

    //nisi napravila na dole:
    public Message acceptFacilityRequest(); // odobrava novo unet sportski objekat od worker    

    public Message changeAthleteAccount();
    public Message changeWorkerAccount();
    public Message deleteAthleteAccount();
    public Message deleteWorkerAccount();
    // za pregled naloga imas getAthlete i getWorker iz userController



}
