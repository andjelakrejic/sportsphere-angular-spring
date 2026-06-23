package com.example.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.TeammateRepo;
import com.example.backend.models.Message;
import com.example.backend.models.TeammateAd;
import com.example.backend.models.TeammateRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/teammates")
@CrossOrigin(origins = "http://localhost:4200")
public class TeammateController {
    
    @GetMapping("/getTeammateAds/{athleteId}")
    public List<TeammateAd> getTeammateAds(@PathVariable int athleteId) {
        return new TeammateRepo().getTeammateAds(athleteId);
    }

    @GetMapping("/getAllActiveAds")
    public List<TeammateAd> getAllActiveAds() {
        return new TeammateRepo().getAllActiveAds();
    }

    @GetMapping("/getTeammateRequests/{adId}")
    public List<TeammateRequest> getTeammateRequests(@PathVariable int adId) {
        return new TeammateRepo().getTeammateRequests(adId);
    }

    @GetMapping("/getTeammateRequest") 
    public ResponseEntity<TeammateRequest> getTeammateRequest(@RequestParam int athleteId, @RequestParam int adId) { 
        TeammateRequest request = new TeammateRepo().getTeammateRequest(athleteId, adId);
        
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request); 
    }

    @PostMapping("/createAd")
    public Message createAd(@RequestParam TeammateAd obj ) {
        return new TeammateRepo().createAd(obj);
    }

    @PostMapping("/closeAd")
    public Message closeAd(@RequestParam int adId) {
        return new TeammateRepo().closeAd(adId);
    }

    @PostMapping("/sendRequest")
    public Message sendRequest(@RequestParam TeammateRequest t) {
        return new TeammateRepo().sendRequest(t);
    }

    @PostMapping("/approveRequest")
    public Message approveRequest(@RequestBody TeammateRequest t) {
        return new TeammateRepo().approveRequest(t);
    }

    @PostMapping("/rejectRequest")
    public Message rejectRequest(@RequestParam int requestId) {
        return new TeammateRepo().rejectRequest(requestId);
    }

}
