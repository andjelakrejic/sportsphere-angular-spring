package com.example.backend.models.helpers;

import java.util.List;

public class FavoriteSportsObject {
    private int athleteId;
    private List<Integer> sportIds;

    public FavoriteSportsObject() {}
    

    public int getAthleteId() { return athleteId; }
    public void setAthleteId(int athleteId) { this.athleteId = athleteId; }

    public List<Integer> getSportIds() { return sportIds; }
    public void setSportIds(List<Integer> sportIds) { this.sportIds = sportIds; }
}
