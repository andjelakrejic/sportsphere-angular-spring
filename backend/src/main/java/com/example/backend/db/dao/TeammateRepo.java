package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Message;
import com.example.backend.models.TeammateAd;
import com.example.backend.models.TeammateRequest;

public class TeammateRepo implements TeammateRepoInterface {

    @Override
    public List<TeammateAd> getTeammateAds(int athleteId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM teammate_ad WHERE status = 'ACTIVE' and athlete_id=?")
        ) {
            stm.setInt(1, athleteId);

            List<TeammateAd> ads = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                TeammateAd ta = new TeammateAd(
                    rs.getInt("id"), 
                    rs.getInt("athlete_id"),
                    rs.getInt("sport_id"),
                    rs.getString("city"),
                    rs.getString("date"),
                    rs.getString("time_slot"),
                    rs.getInt("total_players_needed"),
                    rs.getInt("missing_players"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
                ads.add(ta);
            }
            return ads;   
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TeammateAd> getAllActiveAds() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM teammate_ad WHERE status = 'ACTIVE'")
        ) {
            List<TeammateAd> ads = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                TeammateAd ta = new TeammateAd(
                    rs.getInt("id"), 
                    rs.getInt("athlete_id"),
                    rs.getInt("sport_id"),
                    rs.getString("city"),
                    rs.getString("date"),
                    rs.getString("time_slot"),
                    rs.getInt("total_players_needed"),
                    rs.getInt("missing_players"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
                ads.add(ta);
            }
            return ads;   
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TeammateRequest> getTeammateRequests(int adId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM teammate_request WHERE ad_id=?")
        ) {
            stm.setInt(1, adId);

            List<TeammateRequest> requests = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                TeammateRequest tr = new TeammateRequest(
                    rs.getInt("id"), 
                    rs.getInt("ad_id"),
                    rs.getInt("athlete_id"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
                requests.add(tr);
            }
            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TeammateRequest getTeammateRequest(int athleteId, int adId) {
       try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM teammate_request WHERE ad_id=? and athlete_id=?")
        ) {
            stm.setInt(1, adId);
            stm.setInt(2, athleteId);

            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return new TeammateRequest(
                    rs.getInt("id"), 
                    rs.getInt("ad_id"),
                    rs.getInt("athlete_id"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
            }     
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message createAd(TeammateAd obj) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO teammate_ad (athlete_id, sport_id, city, date, time_slot, total_players_needed, missing_players, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')")
        ) {
            stm.setInt(1, obj.getAthleteId());
            stm.setInt(2, obj.getSportId());
            stm.setString(3, obj.getCity());
            stm.setString(4, obj.getDate());
            stm.setString(5, obj.getTimeSlot());
            stm.setInt(6, obj.getTotalPlayersNeeded());
            stm.setInt(7, obj.getTotalPlayersNeeded()); // missing_players = total na pocetku
            
            if(stm.executeUpdate() > 0) {
                m.setMessage("Successfully created ad!");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error creating ad");
        return m;
    }

    @Override
    public Message closeAd(int adId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE teammate_ad SET status = 'INACTIVE' WHERE id = ?")
        ) {
            stm.setInt(1, adId);
            
            if(stm.executeUpdate() > 0) {
                m.setMessage("Successfully closed ad!");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error closing ad");
        return m;
    }

    @Override
    public Message sendRequest(TeammateRequest t) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO teammate_request (ad_id, athlete_id, status) VALUES (?, ?, 'PENDING')")
        ) {
            stm.setInt(1, t.getAdId());
            stm.setInt(2, t.getAthleteId());

            if(stm.executeUpdate() > 0) {
                m.setMessage("Successfully sent request!");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error sending Request");
        return m;
    }

    @Override
    public Message approveRequest(TeammateRequest t) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection()) {
            // odobri zahtev
            PreparedStatement stm1 = conn.prepareStatement(
                "UPDATE teammate_request SET status = 'APPROVED' WHERE id = ?");
            stm1.setInt(1, t.getId());
            stm1.executeUpdate();

            // smanji missing_players za 1  
            PreparedStatement stm2 = conn.prepareStatement(
                "UPDATE teammate_ad SET missing_players = missing_players - 1 WHERE id = ?");
            stm2.setInt(1, t.getAdId());
            stm2.executeUpdate();

            // ako je missing_players dosao na 0, zatvori oglas
            PreparedStatement stm3 = conn.prepareStatement(
                "UPDATE teammate_ad SET status = 'INACTIVE' WHERE id = ? AND missing_players = 0");
            stm3.setInt(1, t.getAdId());
            stm3.executeUpdate();

            m.setMessage("Successfully approved request!");
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error approving Request");
        return m;
    }

    @Override
    public Message rejectRequest(int requestId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE teammate_request SET status = 'REJECTED' WHERE id = ?")
        ) {
            stm.setInt(1, requestId);

            if(stm.executeUpdate() > 0) {
                m.setMessage("Successfully rejected request!");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error rejecting Request");
        return m;
    }
    
}
