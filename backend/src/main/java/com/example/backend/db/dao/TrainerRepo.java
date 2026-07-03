package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Trainer;

public class TrainerRepo implements TrainerRepoInterface {

    @Override
    public List<Trainer> getTrainersByFacilityAndSport(int facilityId, int sportId) {
        List<Trainer> trainers = new ArrayList<>();

        String sql = "SELECT u.id, u.first_name, u.last_name, u.image, " +
                "t.specialization, t.price_per_hour, t.facility_id, f.name " +
                "FROM trainer t " +
                "JOIN user u ON t.user_id = u.id " +
                "JOIN trainer_sport ts ON ts.trainer_id = t.user_id " +
                "JOIN facility f ON f.id = t.facility_id " +
                "WHERE t.facility_id = ? AND ts.sport_id = ? ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, facilityId);
            stmt.setInt(2, sportId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trainer trainer = new Trainer(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("image"),
                            rs.getString("specialization"),
                            rs.getDouble("price_per_hour"),
                            rs.getInt("facility_id"),
                            rs.getString("name"),
                            0.0
                    );
                    trainers.add(trainer);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainers;
    }
}
