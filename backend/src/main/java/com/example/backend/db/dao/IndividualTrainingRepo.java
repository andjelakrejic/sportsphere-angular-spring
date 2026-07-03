package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.IndividualTraining;

public class IndividualTrainingRepo implements IndividualTrainingRepoInterface{
    
    @Override
    public boolean bookTraining(int athleteId, int trainerId, int facilityId, int sportId, String scheduledAt) {
        String sql = "INSERT INTO individual_training (athlete_id, trainer_id, facility_id, sport_id, scheduled_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, athleteId);
            stmt.setInt(2, trainerId);
            stmt.setInt(3, facilityId);
            stmt.setInt(4, sportId);
            stmt.setString(5, scheduledAt);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<IndividualTraining> getAthleteTrainings(int athleteId) {
        List<IndividualTraining> trainings = new ArrayList<>();

        String sql = "SELECT id, athlete_id, trainer_id, facility_id, sport_id, scheduled_at, " +
                "CASE WHEN scheduled_at < NOW() THEN 'COMPLETED' ELSE 'SCHEDULED' END AS status " +
                "FROM individual_training " +
                "WHERE athlete_id = ? " +
                "ORDER BY scheduled_at DESC";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, athleteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    IndividualTraining training = new IndividualTraining(
                            rs.getInt("id"),
                            rs.getInt("athlete_id"),
                            rs.getInt("trainer_id"),
                            rs.getInt("facility_id"),
                            rs.getInt("sport_id"),
                            rs.getString("scheduled_at"),
                            rs.getString("status")
                    );
                    trainings.add(training);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trainings;
    }
}
