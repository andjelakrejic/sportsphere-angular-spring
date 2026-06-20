package com.example.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Athlete extends User{
    private List<Sport> favoriteSports;

    public Athlete() {}

    public Athlete(int id, String username, String password, String firstname, String lastname,
                   String email, String phone, String profileImage) {
        super(id, username, password, firstname, lastname, email, phone, profileImage, "APPROVED", "ATHLETE");
        this.favoriteSports = new ArrayList<>();
    }

    public List<Sport> getFavoriteSports() { return favoriteSports; }
    public void setFavoriteSports(List<Sport> favoriteSports) { this.favoriteSports = favoriteSports; }

        
}
