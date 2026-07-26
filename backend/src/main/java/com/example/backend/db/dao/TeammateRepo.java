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
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM teammate_ad WHERE athlete_id=? ORDER BY created_at DESC")
        ) {
            stm.setInt(1, athleteId);
            List<TeammateAd> ads = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                ads.add(new TeammateAd(
                    rs.getInt("id"), rs.getInt("athlete_id"), rs.getInt("sport_id"),
                    rs.getString("city"), rs.getString("date"), rs.getString("time_slot"),
                    rs.getInt("total_players_needed"), rs.getInt("missing_players"),
                    rs.getString("status"), rs.getString("created_at")
                ));
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
            PreparedStatement stm = conn.prepareStatement(
                "SELECT ta.*, u.username AS athlete_username " +
                "FROM teammate_ad ta " +
                "JOIN user u ON ta.athlete_id = u.id " +
                "WHERE ta.status = 'ACTIVE'")
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
                ta.setAthleteUsername(rs.getString("athlete_username"));
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
            PreparedStatement stm = conn.prepareStatement(
                "SELECT tr.*, u.username AS athlete_username " +
                "FROM teammate_request tr " +
                "JOIN user u ON tr.athlete_id = u.id " +
                "WHERE tr.ad_id=?")
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
                tr.setAthleteUsername(rs.getString("athlete_username"));
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
                "DELETE FROM teammate_ad WHERE id = ?")
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
        try (Connection conn = DB.source().getConnection()) {
            
            // proveri da li vec postoji zahtev
            PreparedStatement check = conn.prepareStatement(
                "SELECT id FROM teammate_request WHERE ad_id = ? AND athlete_id = ?");
            check.setInt(1, t.getAdId());
            check.setInt(2, t.getAthleteId());
            ResultSet rs = check.executeQuery();
            
            if (rs.next()) {
                m.setMessage("ALREADY_SENT");
                return m;
            }

            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO teammate_request (ad_id, athlete_id, status) VALUES (?, ?, 'PENDING')");
            stm.setInt(1, t.getAdId());
            stm.setInt(2, t.getAthleteId());

            if (stm.executeUpdate() > 0) {
                m.setMessage("OK");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("ERROR");
        return m;
    }

    @Override
    public Message approveRequest(TeammateRequest t) {
        Message m = new Message("");
        
        try (Connection conn = DB.source().getConnection()) {
            // 1. Uključujemo transakciju
            conn.setAutoCommit(false);

            try {
                // 2. Odobri zahtev
                try (PreparedStatement stm1 = conn.prepareStatement(
                        "UPDATE teammate_request SET status = 'APPROVED' WHERE id = ? AND status = 'PENDING'")) {
                    stm1.setInt(1, t.getId());
                    int updatedRows = stm1.executeUpdate();
                    
                    // Ako zahtev nije bio PENDING (već je npr. ranije odobren ili odbijen)
                    if (updatedRows == 0) {
                        conn.rollback();
                        m.setMessage("Request is no longer pending!");
                        return m;
                    }
                }

                // 3. Smanji missing_players za 1 (samo ako ima slobodnih mesta > 0)
                try (PreparedStatement stm2 = conn.prepareStatement(
                        "UPDATE teammate_ad SET missing_players = missing_players - 1 WHERE id = ? AND missing_players > 0")) {
                    stm2.setInt(1, t.getAdId());
                    int updatedAds = stm2.executeUpdate();

                    // Ako nije bilo slobodnih mesta
                    if (updatedAds == 0) {
                        conn.rollback();
                        m.setMessage("Ad is already full!");
                        return m;
                    }
                }

                // 4. Ako je missing_players došao na 0, zatvori oglas
                try (PreparedStatement stm3 = conn.prepareStatement(
                        "UPDATE teammate_ad SET status = 'INACTIVE' WHERE id = ? AND missing_players = 0")) {
                    stm3.setInt(1, t.getAdId());
                    stm3.executeUpdate();
                }

                // Ako je sve prošlo super, potvrditi izmene u bazi
                conn.commit();
                m.setMessage("Successfully approved request!");
                return m;

            } catch (SQLException e) {
                conn.rollback(); // Ako išta pukne unutra, poništi sve izmene
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true); // Vrati na default
            }

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

    @Override
    public List<Integer> getMySentRequests(int athleteId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT ad_id FROM teammate_request WHERE athlete_id = ?")) 
            {
                List<Integer> ids = new ArrayList<>();
                stm.setInt(1, athleteId);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("ad_id"));
                }
                return ids;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
    }

    @Override
    public List<TeammateAd> getMyTeams(int athleteId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT DISTINCT ta.*, u.username AS athlete_username FROM teammate_ad ta " +
                "JOIN user u ON ta.athlete_id = u.id " +
                "JOIN teammate_request tr ON tr.ad_id = ta.id AND tr.status = 'APPROVED' " +
                "WHERE (ta.athlete_id = ? OR tr.athlete_id = ?) " +
                "ORDER BY ta.created_at DESC")
        ) {
            stm.setInt(1, athleteId);
            stm.setInt(2, athleteId);
            List<TeammateAd> ads = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                TeammateAd ta = new TeammateAd(
                    rs.getInt("id"), rs.getInt("athlete_id"), rs.getInt("sport_id"),
                    rs.getString("city"), rs.getString("date"), rs.getString("time_slot"),
                    rs.getInt("total_players_needed"), rs.getInt("missing_players"),
                    rs.getString("status"), rs.getString("created_at")
                );
                ta.setAthleteUsername(rs.getString("athlete_username"));
                ads.add(ta);
            }
            return ads;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TeammateRequest> getApprovedPlayers(int adId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT tr.*, u.username AS athlete_username " +
                "FROM teammate_request tr " +
                "JOIN user u ON tr.athlete_id = u.id " +
                "WHERE tr.ad_id=? AND tr.status='APPROVED'")
        ) {
            stm.setInt(1, adId);
            List<TeammateRequest> requests = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                TeammateRequest tr = new TeammateRequest(
                    rs.getInt("id"), rs.getInt("ad_id"), rs.getInt("athlete_id"),
                    rs.getString("status"), rs.getString("created_at")
                );
                tr.setAthleteUsername(rs.getString("athlete_username"));
                requests.add(tr);
            }
            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message removePlayer(int adId, int athleteId) {
        Message m = new Message("");
        
        String updateRequestSql = "UPDATE teammate_request SET status = 'REJECTED' WHERE ad_id = ? AND athlete_id = ?";
        String updateAdSql = "UPDATE teammate_ad SET missing_players = missing_players + 1, status = 'ACTIVE' WHERE id = ?";

        try (Connection conn = DB.source().getConnection()) {
            // Isključujemo autocommit da bi sve bilo jedna atomska transakcija
            conn.setAutoCommit(false);

            try (PreparedStatement stmRequest = conn.prepareStatement(updateRequestSql);
                PreparedStatement stmAd = conn.prepareStatement(updateAdSql)) {

                // 1. Ažuriramo status igrača u zahtevima
                stmRequest.setInt(1, adId);
                stmRequest.setInt(2, athleteId);
                int rowsRequest = stmRequest.executeUpdate();

                // 2. Oslobađamo mesto u oglasu i vraćamo oglas na ACTIVE
                stmAd.setInt(1, adId);
                int rowsAd = stmAd.executeUpdate();

                if (rowsRequest > 0 && rowsAd > 0) {
                    conn.commit(); // Sve je uspešno promedjeno
                    m.setMessage("Player successfully removed from team!");
                    return m;
                } else {
                    conn.rollback(); // Vraćamo izmene ako je nešto zakazalo
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        m.setMessage("Error removing player from team");
        return m;
    }

    public List<TeammateRequest> getMySentRequestsWithStatus(int athleteId) {
        List<TeammateRequest> requests = new ArrayList<>();
        String sql = "SELECT id, ad_id, athlete_id, status, created_at FROM teammate_request WHERE athlete_id = ?";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setInt(1, athleteId);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    TeammateRequest req = new TeammateRequest();
                    req.setId(rs.getInt("id"));
                    req.setAdId(rs.getInt("ad_id"));
                    req.setAthleteId(rs.getInt("athlete_id"));
                    req.setStatus(rs.getString("status")); // 'PENDING', 'APPROVED', 'REJECTED'
                    req.setCreatedAt(rs.getString("created_at"));

                    requests.add(req);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }
    
}
