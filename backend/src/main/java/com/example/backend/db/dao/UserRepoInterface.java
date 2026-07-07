package com.example.backend.db.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.backend.models.Athlete;
import com.example.backend.models.Message;
import com.example.backend.models.Sport;
import com.example.backend.models.helpers.ChangePasswordObject;
import com.example.backend.models.helpers.FavoriteSportsObject;

public interface UserRepoInterface {

    public Athlete loginAthlete(Athlete a);    
    public String uploadProfileImage(String username, MultipartFile image);
    public Athlete getAthlete(String username);
    public Athlete getAthleteById(int id);

    public Message updateAthlete(Athlete a);
     
    public Message updateFavoriteSports(FavoriteSportsObject obj);
    public Message changePassword(ChangePasswordObject obj);

    // Register
    public int registerAthlete(Athlete a);
    public boolean usernameExists(String username);
    public boolean emailExists(String email);
    
    public void addFavoriteSport(int athleteId, int sportId);
    public List<Sport> getFavoriteSports(int id);
}
