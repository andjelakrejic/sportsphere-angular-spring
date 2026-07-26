package com.example.backend.db.dao;

import java.util.Map;

public interface ChartRepoInterface {
    public Map<String, Integer> countResPerSport(int athleteId);
    public Map<String, Integer> countPlayedResPerSport(int athleteId);
    public Map<String, Integer> reservationsPerMonth(int athleteId);
    public double getTotalEquipmentSpending(int athleteId);
}