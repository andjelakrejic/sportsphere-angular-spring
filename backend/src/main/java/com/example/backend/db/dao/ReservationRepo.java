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

public class ReservationRepo implements ReservationRepoInterface{

    @Override
    public Message createReservation(CreateReservationObject obj) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO reservation (court_id, athlete_id, sport_id, date, time_from, time_to, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'PENDING')"
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
                : new Message("Error creating reservation");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message("Error creating reservation");
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
                : new Message("Error deleting reservation");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message("Error deleting reservation");
    }
    
}
