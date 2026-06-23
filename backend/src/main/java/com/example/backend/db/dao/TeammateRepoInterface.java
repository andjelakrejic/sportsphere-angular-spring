package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.TeammateAd;
import com.example.backend.models.TeammateRequest;

public interface TeammateRepoInterface {
    
    // Ads
    public List<TeammateAd> getTeammateAds (int athleteId);
    public List<TeammateAd> getAllActiveAds();
    Message createAd(TeammateAd obj);
    Message closeAd(int adId);

    // Requests
    public List<TeammateRequest> getTeammateRequests(int adId);
    TeammateRequest getTeammateRequest(int athleteId, int adId);
    Message sendRequest(TeammateRequest t);
    Message approveRequest(TeammateRequest t);
    Message rejectRequest(int requestId);



}
