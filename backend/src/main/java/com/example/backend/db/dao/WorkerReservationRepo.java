package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Message;
import com.example.backend.models.helpers.AthleteBlockStatusDTO;
import com.example.backend.models.helpers.WorkerReservationDTO;
import com.example.backend.models.helpers.WorkerTrainingDTO;

public class WorkerReservationRepo implements WorkerReservationRepoInterface {

    @Override
    public List<WorkerReservationDTO> getReservationsForFacility(int facilityId) {
        expireOverdueReservations(facilityId);

        List<WorkerReservationDTO> list = new ArrayList<>();
        String sql = "SELECT r.id, c.name as court_name, u.username as athlete_username, " +
                "s.name as sport_name, r.date, r.time_from, r.time_to, r.status " +
                "FROM reservation r " +
                "JOIN court c ON r.court_id = c.id " +
                "JOIN athlete a ON r.athlete_id = a.user_id " +
                "JOIN user u ON a.user_id = u.id " +
                "JOIN sport s ON r.sport_id = s.id " +
                "WHERE c.facility_id = ? " +
                "ORDER BY r.date, r.time_from";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, facilityId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                WorkerReservationDTO dto = new WorkerReservationDTO();
                dto.setId(rs.getInt("id"));
                dto.setCourtName(rs.getString("court_name"));
                dto.setAthleteUsername(rs.getString("athlete_username"));
                dto.setSportName(rs.getString("sport_name"));
                dto.setDate(rs.getString("date"));
                dto.setTimeFrom(rs.getString("time_from"));
                dto.setTimeTo(rs.getString("time_to"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<WorkerTrainingDTO> getTrainingsForFacility(int facilityId) {
        expireOverdueTrainings(facilityId);
        List<WorkerTrainingDTO> list = new ArrayList<>();
        String sql = "SELECT t.id, ua.username as athlete_username, ut.username as trainer_username, " +
                    "s.name as sport_name, t.training_date, t.time_from, t.time_to, t.scheduled_at, t.status " +
                    "FROM individual_training t " +
                    "JOIN athlete a ON t.athlete_id = a.user_id " +
                    "JOIN user ua ON a.user_id = ua.id " +
                    "JOIN trainer tr ON t.trainer_id = tr.user_id " +
                    "JOIN user ut ON tr.user_id = ut.id " +
                    "JOIN sport s ON t.sport_id = s.id " +
                    "WHERE t.facility_id = ? " +
                    "ORDER BY t.training_date, t.time_from";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, facilityId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                WorkerTrainingDTO dto = new WorkerTrainingDTO();
                dto.setId(rs.getInt("id"));
                dto.setAthleteUsername(rs.getString("athlete_username"));
                dto.setTrainerUsername(rs.getString("trainer_username"));
                dto.setSportName(rs.getString("sport_name"));
                dto.setTrainingDate(rs.getString("training_date"));
                dto.setTimeFrom(rs.getString("time_from"));
                dto.setTimeTo(rs.getString("time_to"));
                dto.setScheduledAt(rs.getString("scheduled_at"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void expireOverdueReservations(int facilityId) {
        String selectSql = "SELECT r.id, r.athlete_id, c.facility_id " +
                "FROM reservation r JOIN court c ON r.court_id = c.id " +
                "WHERE c.facility_id = ? AND r.status = 'BOOKED' " +
                "AND TIMESTAMPADD(MINUTE, 10, TIMESTAMP(r.date, r.time_from)) < NOW()";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(selectSql)) {
            stm.setInt(1, facilityId);
            ResultSet rs = stm.executeQuery();

            List<int[]> overdue = new ArrayList<>();
            while (rs.next()) {
                overdue.add(new int[]{rs.getInt("id"), rs.getInt("athlete_id"), rs.getInt("facility_id")});
            }

            for (int[] row : overdue) {
                try (PreparedStatement updateStm = conn.prepareStatement(
                        "UPDATE reservation SET status = 'NO_SHOW' WHERE id = ?")) {
                    updateStm.setInt(1, row[0]);
                    updateStm.executeUpdate();
                }
                registerNoShow(conn, row[1], row[2]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // isto za treninge
    private void expireOverdueTrainings(int facilityId) {
        String selectSql = "SELECT id, athlete_id, facility_id " +
                "FROM individual_training " +
                "WHERE facility_id = ? AND status = 'BOOKED' " +
                "AND TIMESTAMPADD(MINUTE, 10, TIMESTAMP(training_date, time_from)) < NOW()";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(selectSql)) {
            stm.setInt(1, facilityId);
            ResultSet rs = stm.executeQuery();

            List<int[]> overdue = new ArrayList<>();
            while (rs.next()) {
                overdue.add(new int[]{rs.getInt("id"), rs.getInt("athlete_id"), rs.getInt("facility_id")});
            }

            for (int[] row : overdue) {
                try (PreparedStatement updateStm = conn.prepareStatement(
                        "UPDATE individual_training SET status = 'NO_SHOW' WHERE id = ?")) {
                    updateStm.setInt(1, row[0]);
                    updateStm.executeUpdate();
                }
                registerNoShow(conn, row[1], row[2]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message confirmReservation(int reservationId) {
        String sql = "UPDATE reservation SET status = 'CONFIRMED' WHERE id = ? AND status = 'BOOKED'";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, reservationId);
            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message(true, "Reservation confirmed.")
                : new Message(false, "Reservation not found or already handled.");
        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Message markNoShowReservation(int reservationId) {
        String getSql = "SELECT r.athlete_id, c.facility_id " +
                         "FROM reservation r JOIN court c ON r.court_id = c.id " +
                         "WHERE r.id = ? AND r.status = 'BOOKED'";
        String updateSql = "UPDATE reservation SET status = 'NO_SHOW' WHERE id = ?";

        try (Connection conn = DB.source().getConnection()) {
            int athleteId;
            int facilityId;

            try (PreparedStatement getStm = conn.prepareStatement(getSql)) {
                getStm.setInt(1, reservationId);
                ResultSet rs = getStm.executeQuery();
                if (!rs.next()) {
                    return new Message(false, "Reservation not found or already handled.");
                }
                athleteId = rs.getInt("athlete_id");
                facilityId = rs.getInt("facility_id");
            }

            try (PreparedStatement updateStm = conn.prepareStatement(updateSql)) {
                updateStm.setInt(1, reservationId);
                updateStm.executeUpdate();
            }

            return registerNoShow(conn, athleteId, facilityId);

        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Message confirmTraining(int trainingId) {
        String sql = "UPDATE individual_training SET status = 'CONFIRMED' WHERE id = ? AND status = 'BOOKED'";
        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, trainingId);
            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message(true, "Training confirmed.")
                : new Message(false, "Training not found or already handled.");
        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    @Override
    public Message markNoShowTraining(int trainingId) {
        String getSql = "SELECT athlete_id, facility_id FROM individual_training " +
                         "WHERE id = ? AND status = 'BOOKED'";
        String updateSql = "UPDATE individual_training SET status = 'NO_SHOW' WHERE id = ?";

        try (Connection conn = DB.source().getConnection()) {
            int athleteId;
            int facilityId;

            try (PreparedStatement getStm = conn.prepareStatement(getSql)) {
                getStm.setInt(1, trainingId);
                ResultSet rs = getStm.executeQuery();
                if (!rs.next()) {
                    return new Message(false, "Training not found or already handled.");
                }
                athleteId = rs.getInt("athlete_id");
                facilityId = rs.getInt("facility_id");
            }

            try (PreparedStatement updateStm = conn.prepareStatement(updateSql)) {
                updateStm.setInt(1, trainingId);
                updateStm.executeUpdate();
            }

            return registerNoShow(conn, athleteId, facilityId);

        } catch (SQLException e) {
            return new Message(false, "Database error: " + e.getMessage());
        }
    }

    // deljena logika - upisuje/uvecava no-show broj i blokira ako je dostignut max_no_shows
    private Message registerNoShow(Connection conn, int athleteId, int facilityId) throws SQLException {
        int maxNoShows = 0;
        try (PreparedStatement facilityStm = conn.prepareStatement(
                "SELECT max_no_shows FROM facility WHERE id = ?")) {
            facilityStm.setInt(1, facilityId);
            ResultSet rs = facilityStm.executeQuery();
            if (rs.next()) {
                maxNoShows = rs.getInt("max_no_shows");
            }
        }

        try (PreparedStatement upsertStm = conn.prepareStatement(
                "INSERT INTO athlete_facility_block (athlete_id, facility_id, no_show_count, blocked) " +
                "VALUES (?, ?, 1, FALSE) " +
                "ON DUPLICATE KEY UPDATE no_show_count = no_show_count + 1")) {
            upsertStm.setInt(1, athleteId);
            upsertStm.setInt(2, facilityId);
            upsertStm.executeUpdate();
        }

        try (PreparedStatement blockStm = conn.prepareStatement(
                "UPDATE athlete_facility_block SET blocked = TRUE " +
                "WHERE athlete_id = ? AND facility_id = ? AND no_show_count >= ?")) {
            blockStm.setInt(1, athleteId);
            blockStm.setInt(2, facilityId);
            blockStm.setInt(3, maxNoShows);
            blockStm.executeUpdate();
        }

        return new Message(true, "Marked as no-show.");
    }

    @Override
    public AthleteBlockStatusDTO getBlockStatus(int athleteId, int facilityId) {
        int maxNoShows = 0;
        String facilitySql = "SELECT max_no_shows FROM facility WHERE id = ?";

        String blockSql = "SELECT no_show_count, blocked FROM athlete_facility_block " +
                        "WHERE athlete_id = ? AND facility_id = ?";

        try (Connection conn = DB.source().getConnection()) {

            try (PreparedStatement facilityStm = conn.prepareStatement(facilitySql)) {
                facilityStm.setInt(1, facilityId);
                ResultSet rs = facilityStm.executeQuery();
                if (rs.next()) {
                    maxNoShows = rs.getInt("max_no_shows");
                }
            }

            try (PreparedStatement blockStm = conn.prepareStatement(blockSql)) {
                blockStm.setInt(1, athleteId);
                blockStm.setInt(2, facilityId);
                ResultSet rs = blockStm.executeQuery();
                if (rs.next()) {
                    return new AthleteBlockStatusDTO(rs.getBoolean("blocked"), rs.getInt("no_show_count"), maxNoShows);
                }
            }

            // atleta jos nema red u athlete_facility_block -> nema no-show-ova, nije blokiran
            return new AthleteBlockStatusDTO(false, 0, maxNoShows);

        } catch (SQLException e) {
            e.printStackTrace();
            // u slucaju greske, ne blokiramo korisnika da ne bismo neopravdano sprecili rezervaciju
            return new AthleteBlockStatusDTO(false, 0, 0);
        }
    }
}