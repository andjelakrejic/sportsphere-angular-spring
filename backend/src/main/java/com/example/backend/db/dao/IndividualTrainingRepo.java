package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.IndividualTraining;
import com.example.backend.models.Message;
import com.example.backend.models.helpers.CourtTrainingViewDTO;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

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

    @Override
    public List<CourtTrainingViewDTO> getTrainingsForCourt(GetReservationObject obj) {
        List<CourtTrainingViewDTO> trainings = new ArrayList<>();
        String sql = "SELECT id, training_date, time_from, time_to, " +
                "CASE WHEN training_date < CURDATE() THEN 'COMPLETED' ELSE 'SCHEDULED' END AS status " +
                "FROM individual_training " +
                "WHERE court_id = ? AND training_date BETWEEN ? AND ? ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, obj.getCourtId());
            stmt.setDate(2, java.sql.Date.valueOf(obj.getWeekStart()));
            stmt.setDate(3, java.sql.Date.valueOf(obj.getWeekEnd()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = rs.getDate("training_date").toLocalDate();
                    LocalTime from = rs.getTime("time_from").toLocalTime();
                    LocalTime to = rs.getTime("time_to").toLocalTime();

                    trainings.add(new CourtTrainingViewDTO(
                        rs.getInt("id"),
                        LocalDateTime.of(date, from),
                        LocalDateTime.of(date, to),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainings;
    }

    @Override
    public Message updateTrainingTime(UpdateTimeObject obj) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE individual_training SET training_date = ?, time_from = ?, time_to = ? WHERE id = ?"
            )) {
            stm.setDate(1, java.sql.Date.valueOf(obj.getDate()));
            stm.setTime(2, java.sql.Time.valueOf(obj.getStartTime()));
            stm.setTime(3, java.sql.Time.valueOf(obj.getEndTime()));
            stm.setInt(4, obj.getId());

            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message("Training moved successfully!")
                : new Message(false, "Error moving training");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, "Error moving training");
    }
}
