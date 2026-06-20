package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.PromotionRepo;
import com.example.backend.models.Promotion;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/promotions")
@CrossOrigin(origins = "http://localhost:4200")
public class PromotionController {

    @GetMapping("/getActivePromotions")
    public List<Promotion> getActivePromotions() {
        return new PromotionRepo().getActivePromotions();
    }

}
