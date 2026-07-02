package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.backend.db.DB;

public class ChartRepo implements ChartRepoInterface {

    @Override
    public Map<String, Integer> countResPerSport(int athleteId) {
        String sql =    "SELECT s.name, COUNT(*) AS cnt FROM reservation r " +
                        "JOIN sport s ON s.id = r.sport_id " +
                        "WHERE r.athlete_id = ? AND r.status != 'DENIED' " +
                        "GROUP BY s.name ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)
        ) {
            Map<String, Integer> result = new HashMap<>();

            stm.setInt(1, athleteId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("cnt"));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, Integer> countPlayedResPerSport(int athleteId) {
        String sql =    "SELECT s.name, COUNT(*) AS cnt FROM reservation r " +
                        "JOIN sport s ON s.id = r.sport_id " +
                        "WHERE r.athlete_id = ? AND r.status = 'CONFIRMED' AND r.date < CURDATE() " +
                        "GROUP BY s.name ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)
        ) {
            Map<String, Integer> result = new HashMap<>();

            stm.setInt(1, athleteId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("cnt"));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<Integer, Integer> reservationsPerMonth(int athleteId) {
        String sql =    "SELECT EXTRACT(MONTH FROM r.date) AS res_month, COUNT(*) AS cnt " +
                        "FROM reservation r " +
                        "WHERE r.athlete_id = ? AND r.status != 'DENIED' " +
                        "GROUP BY EXTRACT(MONTH FROM r.date) " +
                        "ORDER BY res_month ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)
        ) {
            Map<Integer, Integer> result = new LinkedHashMap<>();

            stm.setInt(1, athleteId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                result.put(rs.getInt("res_month"), rs.getInt("cnt"));
            }
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double getTotalEquipmentSpending() {
        String sql =    "SELECT SUM(e.price_at_purchase * e.quantity) AS total FROM equipment_orders e " +
                        "JOIN orders o ON o.id = e.order_id " +
                        "WHERE o.status = 'PICKED UP' ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}