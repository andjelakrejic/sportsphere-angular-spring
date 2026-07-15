package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.PromotionRepo;
import com.example.backend.models.Message;
import com.example.backend.models.Promotion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/promotions")
@CrossOrigin(origins = "http://localhost:4200")
public class PromotionController {

    @GetMapping("/getActivePromotions")
    public List<Promotion> getActivePromotions() {
        return new PromotionRepo().getActivePromotions();
    }

    @GetMapping("/getPromotionsByFacility/{facilityId}")
    public List<Promotion> getPromotionsByFacility(@PathVariable int facilityId) {
        return new PromotionRepo().getPromotionsByFacility(facilityId);
    }

    @PostMapping("/addPromotion")
    public Message addPromotion(@RequestBody Promotion p) {
        return new PromotionRepo().addPromotion(p);
    }

    @PutMapping("/updatePromotion")
    public Message updatePromotion(@RequestBody Promotion p) {
        return new PromotionRepo().updatePromotion(p);
    }

}
