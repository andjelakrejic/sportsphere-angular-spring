package com.example.backend.models.helpers;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSportsObject {
    String username;
    List<String> sports;
    
    public FavoriteSportsObject(String username, List<String> sports) {
        this.sports = new ArrayList<>();
        this.username = username;
        this.sports = sports;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    
    
}
