package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Promotion;

public interface PromotionRepoInterface {
    public List<Promotion> getActivePromotions();
}
