package com.example.backend.db.dao;

import java.util.Map;

public interface ChartRepoInterface {
    Map<String, Integer> countResPerSport(int athleteId);
    Map<String, Integer> countPlayedResPerSport(int athleteId);
    Map<Integer, Integer> reservationsPerMonth(int athleteId);
    double getTotalEquipmentSpending();
}