package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.TeammateAd;
import com.example.backend.models.TeammateRequest;

public interface TeammateRepoInterface {
    
    // Ads
    public List<TeammateAd> getTeammateAds (int athleteId);
    public List<TeammateAd> getAllActiveAds();
    public Message createAd(TeammateAd obj);
    public Message closeAd(int adId);

    // Requests
    public List<TeammateRequest> getTeammateRequests(int adId);
    public TeammateRequest getTeammateRequest(int athleteId, int adId);
    public Message sendRequest(TeammateRequest t);
    public Message approveRequest(TeammateRequest t);
    public Message rejectRequest(int requestId);
    public List<Integer> getMySentRequests(int athleteId);

    public List<TeammateAd> getMyTeams(int athleteId);
    public List<TeammateRequest> getApprovedPlayers(int adId);

}
