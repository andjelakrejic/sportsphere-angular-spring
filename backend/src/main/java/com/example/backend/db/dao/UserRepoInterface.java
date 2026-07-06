package com.example.backend.db.dao;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Worker;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

public interface UserRepoInterface {

    public Athlete loginAthlete(Athlete a);
    public Worker loginWorker(Worker a);
    
    public int registerAthlete(Athlete a);
    public int registerWorker(Worker w);
    public String uploadProfileImage(String username, MultipartFile image);
    // public int registerAthleteWithImage(Athlete a);
    // public int registerWorkerWithImage(Worker w);

    public Worker getWorker(String username);
    public Athlete getAthlete(String username);
    public Athlete getAthleteById(int id);

    public Message updateAthlete(Athlete a);
    public Message updateFavoriteSports(FavoriteSportsObject obj);
    public Message changePassword(ChangePasswordObject obj);

    // Register

    public boolean usernameExists(String username);
    public boolean emailExists(String email);
    public boolean maticniBrojExists(String mb);
    public boolean pibExists(String pib);
    public void addFavoriteSport(int athleteId, int sportId);
    public int countWorkersAtFacility(String facilityName, String address);
}
