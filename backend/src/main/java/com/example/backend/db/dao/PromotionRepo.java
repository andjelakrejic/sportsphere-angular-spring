package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Message;
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

    @Override
    public List<Promotion> getPromotionsByFacility(int facilityId) { // jer jedan radnik ima vise facility
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT p.*, f.name as facility_name, s.name as sport_name FROM promotion p " +
                "JOIN facility f ON p.facility_id = f.id " +
                "JOIN sport s ON p.sport_id = s.id " +
                "WHERE p.facility_id = ? " +
                "ORDER BY p.date_from DESC "
            );
        ) {
            stm.setInt(1, facilityId);
            List<Promotion> promotions = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                promotions.add(new Promotion(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("facility_id"),
                    rs.getString("facility_name"),
                    rs.getInt("sport_id"),
                    rs.getString("sport_name"),
                    rs.getString("date_from"),
                    rs.getString("date_to"),
                    rs.getString("discount_type"),
                    rs.getDouble("discount_value")
                ));
            }
            return promotions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message addPromotion(Promotion p) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO promotion (facility_id, name, sport_id, date_from, date_to, discount_type, discount_value) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) "
            );
        ) {
            stm.setInt(1, p.getFacilityId());
            stm.setString(2, p.getName());
            stm.setInt(3, p.getSportId());
            stm.setString(4, p.getDateFrom());
            stm.setString(5, p.getDateTo());
            stm.setString(6, p.getDiscountType());
            stm.setDouble(7, p.getDiscountValue());
            stm.executeUpdate();
            return new Message("Promotion successfully added");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false,"Error when adding promotion");
        }
    }

    @Override
    public Message updatePromotion(Promotion p) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE promotion SET name = ?, sport_id = ?, date_from = ?, date_to = ?, " +
                "discount_type = ?, discount_value = ? WHERE id = ? "
            );
        ) {
            stm.setString(1, p.getName());
            stm.setInt(2, p.getSportId());
            stm.setString(3, p.getDateFrom());
            stm.setString(4, p.getDateTo());
            stm.setString(5, p.getDiscountType());
            stm.setDouble(6, p.getDiscountValue());
            stm.setInt(7, p.getId());
            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message("Promotion successfully updated")
                : new Message(false,"Promotion not found");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false, "Error when updating promotion");
        }
    }
    
}
