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
import com.example.backend.models.helpers.BookTrainingRequest;
import com.example.backend.models.helpers.CourtTrainingViewDTO;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

public class IndividualTrainingRepo implements IndividualTrainingRepoInterface{

    @Override
    public List<IndividualTraining> getAthleteTrainings(int athleteId) {
        List<IndividualTraining> trainings = new ArrayList<>();

        String sql = "SELECT it.id, it.athlete_id, it.trainer_id, it.facility_id, it.sport_id, it.scheduled_at, " +
                "it.training_date, it.time_from, it.time_to, it.status, " +  // ← direktno iz baze
                "u.username AS trainer_name, f.name AS facility_name, s.name AS sport_name " +
                "FROM individual_training it " +
                "JOIN trainer t ON it.trainer_id = t.user_id " +
                "JOIN user u ON t.user_id = u.id " +
                "JOIN facility f ON it.facility_id = f.id " +
                "JOIN sport s ON it.sport_id = s.id " +
                "WHERE it.athlete_id = ? " +
                "ORDER BY it.training_date DESC, it.time_from DESC";

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
                    training.setTrainingDate(rs.getString("training_date"));
                    training.setTimeFrom(rs.getString("time_from"));
                    training.setTimeTo(rs.getString("time_to"));
                    training.setTrainerName(rs.getString("trainer_name"));
                    training.setFacilityName(rs.getString("facility_name"));
                    training.setSportName(rs.getString("sport_name"));
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
                "CASE WHEN training_date < CURDATE() THEN 'COMPLETED' ELSE 'BOOKED' END AS status " +
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

    @Override
    public Message bookTraining(BookTrainingRequest req) {
        try (Connection conn = DB.source().getConnection()) {

            // 1. Nadji slobodan court koji odgovara sportu i nije zauzet u tom terminu
            String findCourtSql =
                "SELECT c.id FROM court c " +
                "WHERE c.facility_id = ? AND c.sport_id = ? " +
                "AND c.id NOT IN ( " +
                "  SELECT r.court_id FROM reservation r " +
                "  WHERE r.date = ? AND r.status != 'CANCELLED' " +
                "  AND NOT (r.time_to <= ? OR r.time_from >= ?) " +
                ") " +
                "AND c.id NOT IN ( " +
                "  SELECT it.court_id FROM individual_training it " +
                "  WHERE it.training_date = ? AND it.court_id IS NOT NULL " +
                "  AND NOT (it.time_to <= ? OR it.time_from >= ?) " +
                ") " +
                "LIMIT 1";

            Integer courtId = null;
            try (PreparedStatement stm = conn.prepareStatement(findCourtSql)) {
                stm.setInt(1, req.getFacilityId());
                stm.setInt(2, req.getSportId());
                stm.setDate(3, java.sql.Date.valueOf(req.getDate()));
                stm.setTime(4, java.sql.Time.valueOf(req.getStartTime()));
                stm.setTime(5, java.sql.Time.valueOf(req.getEndTime()));
                stm.setDate(6, java.sql.Date.valueOf(req.getDate()));
                stm.setTime(7, java.sql.Time.valueOf(req.getStartTime()));
                stm.setTime(8, java.sql.Time.valueOf(req.getEndTime()));

                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    courtId = rs.getInt("id");
                }
            }

            if (courtId == null) {
                return new Message(false, "No available court for this time slot.");
            }

            // 2. Insertuj trening sa dodeljenim court_id, scheduled_at = trenutak kreiranja
            String insertSql =
                "INSERT INTO individual_training " +
                "(athlete_id, trainer_id, facility_id, sport_id, scheduled_at, training_date, time_from, time_to, court_id, status) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?, ?, ?, 'BOOKED')";

            try (PreparedStatement stm = conn.prepareStatement(insertSql)) {
                stm.setInt(1, req.getAthleteId());
                stm.setInt(2, req.getTrainerId());
                stm.setInt(3, req.getFacilityId());
                stm.setInt(4, req.getSportId());
                stm.setDate(5, java.sql.Date.valueOf(req.getDate()));
                stm.setTime(6, java.sql.Time.valueOf(req.getStartTime()));
                stm.setTime(7, java.sql.Time.valueOf(req.getEndTime()));
                stm.setInt(8, courtId);

                int rows = stm.executeUpdate();
                return rows > 0
                    ? new Message("Training booked successfully!")
                    : new Message(false, "Booking failed. Please try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, "Booking failed. Please try again.");
    }
}
