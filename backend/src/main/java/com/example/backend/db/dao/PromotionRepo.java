package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Promotion;

public class PromotionRepo implements PromotionRepoInterface{

    @Override
    public List<Promotion> getActivePromotions() { // 3 promocije gde je datum aktivan
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT p.*, f.name as facility_name FROM promotion p " +
                "JOIN facility f ON p.facility_id = f.id " +
                "WHERE p.date_from <= CURDATE() AND p.date_to >= CURDATE() " +
                "LIMIT 3"
            );
        ) {
            List<Promotion> promotions = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Promotion p = new Promotion(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("facility_name"),  // iz JOIN-a
                    rs.getString("date_from"),
                    rs.getString("date_to"),
                    rs.getString("discount_type"),
                    rs.getDouble("discount_value")
                );
                promotions.add(p);
            }
            return promotions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
