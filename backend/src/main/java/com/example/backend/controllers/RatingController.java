package com.example.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.example.backend.db.dao.RatingRepo;
import com.example.backend.models.FacilityReaction;
import com.example.backend.models.Message;

@RestController
@RequestMapping("/ratings")
@CrossOrigin(origins = "http://localhost:4200")
public class RatingController {

    @PostMapping("/addReaction")
    public Message addReaction(@RequestParam int athleteId, @RequestParam int facilityId, @RequestParam String type) {
        return new RatingRepo().addReaction(athleteId, facilityId, type);
    }

    @PostMapping("/addComment")
    public Message addComment(@RequestBody FacilityReaction obj) {
        return new RatingRepo().addComment(obj.getAthleteId(), obj.getFacilityId(), obj.getComment());
    }

    @GetMapping("/getLast5Comments")
    public List<FacilityReaction> getLast5Comments(@RequestParam int facilityId, @RequestParam int athleteId) {
        return new RatingRepo().getLast5Comments(facilityId, athleteId);
    }

    @GetMapping("/getSummary/{facilityId}")
    public Map<String, Integer> getSummary(@PathVariable int facilityId) {
        return new RatingRepo().getSummary(facilityId);
    }

    @GetMapping("/getMyComments/{athleteId}")
    public List<FacilityReaction> getMyComments(@PathVariable int athleteId) {
        return new RatingRepo().getCommentsByAthlete(athleteId);
    }

    @GetMapping("/getMyReaction")
    public String getMyReaction(@RequestParam int athleteId, @RequestParam int facilityId) {
        return new RatingRepo().getMyReaction(athleteId, facilityId);
    }
}