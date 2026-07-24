package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Message;
import com.example.backend.models.Trainer;

public class TrainerRepo implements TrainerRepoInterface {

    @Override
    public List<Trainer> getTrainersByFacilityAndSport(int facilityId, int sportId) {
        List<Trainer> trainers = new ArrayList<>();

        String sql = "SELECT u.id, u.firstname, u.lastname, u.profile_image, " +
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

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trainer trainer = new Trainer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("profile_image"),
                        rs.getString("specialization"),
                        rs.getDouble("price_per_hour"),
                        rs.getInt("facility_id"),
                        rs.getString("name"),
                        0.0
                );
                trainers.add(trainer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainers;
    }

   @Override
    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();

        String sql = "SELECT u.id, u.firstname, u.lastname, u.profile_image, " +
                    "t.specialization, t.price_per_hour, t.facility_id, f.name, t.status " +
                    "FROM trainer t " +
                    "JOIN user u ON t.user_id = u.id " +
                    "JOIN facility f ON f.id = t.facility_id " +
                    "ORDER BY t.status ASC, u.firstname ASC";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Trainer trainer = new Trainer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("profile_image"),
                        rs.getString("specialization"),
                        rs.getDouble("price_per_hour"),
                        rs.getInt("facility_id"),
                        rs.getString("name"),
                        0.0
                );
                trainer.setStatus(rs.getString("status"));
                trainers.add(trainer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainers;
    }

    @Override
    public Message deactivateTrainer(int trainerId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE trainer SET status='INACTIVE' WHERE user_id=?"
            );
        ) {
            stm.setInt(1, trainerId);

            int x = stm.executeUpdate();
            if (x > 0) m.setMessage("Trainer deactivated successfully!");
            else m.setMessage("Error deactivating trainer...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }
}
