package com.example.backend.db.dao;

import java.util.List;
import java.util.Map;

import com.example.backend.models.FacilityReaction;
import com.example.backend.models.Message;

public interface RatingRepoInterface {

    public Message addReaction(int athleteId, int facilityId, String type);
    public String getMyReaction(int athleteId, int facilityId);
    public Message addComment(int athleteId, int facilityId, String commentText);
    public List<FacilityReaction> getLast5Comments(int facilityId, int loggedInAthleteId);
    public Map<String, Integer> getSummary(int facilityId);
    public List<FacilityReaction> getCommentsByAthlete(int athleteId);

    
}
