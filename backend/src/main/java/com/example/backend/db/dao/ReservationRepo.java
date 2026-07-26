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
import com.example.backend.models.Message;
import com.example.backend.models.Reservation;
import com.example.backend.models.helpers.CreateReservationObject;
import com.example.backend.models.helpers.GetReservationObject;
import com.example.backend.models.helpers.UpdateTimeObject;

public class ReservationRepo implements ReservationRepoInterface{

    @Override
    public Message createReservation(CreateReservationObject obj) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO reservation (court_id, athlete_id, sport_id, date, time_from, time_to, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'BOOKED')"
            );
        ) {
            stm.setInt(1, obj.getCourtId());
            stm.setInt(2, obj.getAthleteId());
            stm.setInt(3, obj.getSportId());
            stm.setDate(4, java.sql.Date.valueOf(obj.getDate()));
            stm.setTime(5, java.sql.Time.valueOf(obj.getStartTime()));
            stm.setTime(6, java.sql.Time.valueOf(obj.getEndTime()));

            int rows = stm.executeUpdate();
            return rows > 0 
                ? new Message("Reservation successfully created!") 
                : new Message(false, "Error creating reservation");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, "Error creating reservation");
    }

    @Override
    public List<Reservation> getReservations(int athleteId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT r.id, f.name AS facility_name, f.city, c.name AS court_name, " +
                "s.name AS sport, r.date, r.time_from, r.time_to, r.status " +
                "FROM reservation r " +
                "JOIN court c ON r.court_id = c.id " +
                "JOIN facility f ON c.facility_id = f.id " +
                "JOIN sport s ON r.sport_id = s.id " +
                "WHERE r.athlete_id = ? " +
                "ORDER BY r.date DESC, r.time_from DESC"
            );
        ) {
            List<Reservation> reservations = new ArrayList<>();

            stm.setInt(1, athleteId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime timeFrom = rs.getTime("time_from").toLocalTime();
                LocalTime timeTo = rs.getTime("time_to").toLocalTime();

                Reservation res = new Reservation(
                    rs.getInt("id"),
                    rs.getString("facility_name"),
                    rs.getString("city"),
                    rs.getString("court_name"),
                    rs.getString("sport"),
                    LocalDateTime.of(date, timeFrom),  // spaja date + time_from
                    LocalDateTime.of(date, timeTo),    // spaja date + time_to
                    rs.getString("status")
                );
                reservations.add(res);
            }
            return reservations;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message cancelReservation(int resId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("DELETE FROM reservation WHERE id=?")
        ) {
            stm.setInt(1, resId);
            int rows = stm.executeUpdate();
            return rows > 0 
                ? new Message("Reservation successfully deleted!") 
                : new Message(false,"Error deleting reservation");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false,"Error deleting reservation");
    }

    @Override
    public List<Reservation> getReservationsForCourt(GetReservationObject obj) {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM reservation WHERE court_id = ? AND date BETWEEN ? AND ? " +
                "AND status != 'CANCELLED'"
            );
        ) {
            stm.setInt(1, obj.getCourtId());
            stm.setDate(2, java.sql.Date.valueOf(obj.getWeekStart()));
            stm.setDate(3, java.sql.Date.valueOf(obj.getWeekEnd()));

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime timeFrom = rs.getTime("time_from").toLocalTime();
                LocalTime timeTo = rs.getTime("time_to").toLocalTime();

                reservations.add(new Reservation(
                    rs.getInt("id"),
                    null, null, null, null, // ne trebaju nam za kalendar
                    LocalDateTime.of(date, timeFrom),
                    LocalDateTime.of(date, timeTo),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    @Override
    public Message updateReservationTime(UpdateTimeObject obj) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE reservation SET date = ?, time_from = ?, time_to = ? WHERE id = ?"
            )) {
            stm.setDate(1, java.sql.Date.valueOf(obj.getDate()));
            stm.setTime(2, java.sql.Time.valueOf(obj.getStartTime()));
            stm.setTime(3, java.sql.Time.valueOf(obj.getEndTime()));
            stm.setInt(4, obj.getId());

            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message("Reservation moved successfully!")
                : new Message(false, "Error moving reservation");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, "Error moving reservation");
    }
    
}
