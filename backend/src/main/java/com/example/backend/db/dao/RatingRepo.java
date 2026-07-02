package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.models.FacilityReaction;
import com.example.backend.models.Message;

public class RatingRepo implements RatingRepoInterface{

    @Override
    public Message addReaction(int athleteId, int facilityId, String type) {
        if (!type.equals("LIKE") && !type.equals("DISLIKE")) {
            return new Message(false, "Unknown reaction type.");
        }

        try {
            int confirmedCount = getConfirmedReservationCount(athleteId, facilityId);
            if (confirmedCount < 1) {
                return new Message(false, "You need at least one confirmed reservation at this facility to react.");
            }

            String existingType = getExistingReactionType(athleteId, facilityId);

            if (existingType != null && existingType.equals(type)) {
                // clicking the same reaction again removes it
                String sql = "DELETE FROM facility_reaction " +
                            "WHERE athlete_id = ? AND facility_id = ? AND type IN ('LIKE','DISLIKE') ";
                try (Connection conn = DB.source().getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, athleteId);
                    ps.setInt(2, facilityId);
                    ps.executeUpdate();
                }
                return new Message(true, "Reaction removed.");
            } else if (existingType != null) {
                // switching from LIKE to DISLIKE or vice versa
                String sql = "UPDATE facility_reaction SET type = ? " +
                            "WHERE athlete_id = ? AND facility_id = ? AND type IN ('LIKE','DISLIKE') ";
                try (Connection conn = DB.source().getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, type);
                    ps.setInt(2, athleteId);
                    ps.setInt(3, facilityId);
                    ps.executeUpdate();
                }
            } else {
                // no existing reaction, insert new
                String sql = "INSERT INTO facility_reaction (athlete_id, facility_id, type) " +
                            "VALUES (?, ?, ?) ";
                try (Connection conn = DB.source().getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, athleteId);
                    ps.setInt(2, facilityId);
                    ps.setString(3, type);
                    ps.executeUpdate();
                }
            }

            return new Message(true, "Reaction saved.");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false, "Error saving reaction.");
        }

    }

    @Override
    public String getMyReaction(int athleteId, int facilityId) {    
        return getExistingReactionType(athleteId, facilityId);
    }

    @Override
    public Message addComment(int athleteId, int facilityId, String commentText) {
        if (commentText == null || commentText.trim().isEmpty()) {
            return new Message(false, "The comment cannot be left empty");
        }

        try {
            int confirmedCount = getConfirmedReservationCount(athleteId, facilityId);
            if (confirmedCount < 1) {
                return new Message(false, "You need at least one confirmed reservation at this facility to react.");
            }

            int commentCount = getCommentCount(athleteId, facilityId);
            if (commentCount >= confirmedCount) {
                return new Message(false, "You have reached the maximum amount of comments for this facility.");
            }

            String sql = "INSERT INTO facility_reaction (athlete_id, facility_id, type, comment) " +
                        "VALUES (?, ?, 'COMMENT', ?) ";
            try (Connection conn = DB.source().getConnection();
                PreparedStatement stm = conn.prepareStatement(sql)) {
                stm.setInt(1, athleteId);
                stm.setInt(2, facilityId);
                stm.setString(3, commentText.trim());
                stm.executeUpdate();
            }

            return new Message(true, "Comment added!");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false, "Error occured when adding comment");
        }
    }
    
    @Override
    public List<FacilityReaction> getLast5Comments(int facilityId, int loggedInAthleteId) {
        List<FacilityReaction> result = new ArrayList<>();
        String sql = "SELECT fr.id, fr.athlete_id, fr.comment, fr.created_at, u.firstname, u.lastname " +
                    "FROM facility_reaction fr " +
                    "JOIN user u ON fr.athlete_id = u.id " +
                    "WHERE fr.facility_id = ? AND fr.type = 'COMMENT' " +
                    "ORDER BY fr.created_at DESC " +
                    "LIMIT 5 ";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, facilityId);
            
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                FacilityReaction fr = new FacilityReaction();
                fr.setId(rs.getInt("id"));
                fr.setAthleteId(rs.getInt("athlete_id"));
                fr.setComment(rs.getString("comment"));
                fr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                fr.setAthleteName(rs.getString("firstname") + " " + rs.getString("lastname"));
                fr.setOwnComment(rs.getInt("athlete_id") == loggedInAthleteId);
                result.add(fr);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, Integer> getSummary(int facilityId) {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("likes", countByType(facilityId, "LIKE"));
        summary.put("dislikes", countByType(facilityId, "DISLIKE"));
        return summary;
    }

    private int countByType(int facilityId, String type) {
        String sql = "SELECT COUNT(*) AS cnt FROM facility_reaction " +
                     "WHERE facility_id = ? AND type = ? ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, facilityId);
            stm.setString(2, type);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getInt("cnt");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getConfirmedReservationCount(int athleteId, int facilityId) {
        String sql = "SELECT COUNT(*) AS cnt " +
                     "FROM reservation r " +
                     "JOIN court c ON r.court_id = c.id " +
                     "WHERE r.athlete_id = ? AND c.facility_id = ? AND r.status = 'CONFIRMED' ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, athleteId);
            stm.setInt(2, facilityId);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getInt("cnt");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getExistingReactionType(int athleteId, int facilityId) {
        String sql = "SELECT type FROM facility_reaction " +
                     "WHERE athlete_id = ? AND facility_id = ? AND type IN ('LIKE','DISLIKE') ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, athleteId);
            stm.setInt(2, facilityId);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getString("type");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    private int getCommentCount(int athleteId, int facilityId) {
        String sql = "SELECT COUNT(*) AS cnt FROM facility_reaction " +
                     "WHERE athlete_id = ? AND facility_id = ? AND type = 'COMMENT' ";

        try (Connection conn = DB.source().getConnection();
             PreparedStatement stm = conn.prepareStatement(sql)) {
            stm.setInt(1, athleteId);
            stm.setInt(2, facilityId);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getInt("cnt");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<FacilityReaction> getCommentsByAthlete(int athleteId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT fr.id, fr.facility_id, f.name AS facility_name, fr.comment, fr.created_at " +
                "FROM facility_reaction fr " +
                "JOIN facility f ON fr.facility_id = f.id " +
                "WHERE fr.athlete_id = ? AND fr.type = 'COMMENT' " +
                "ORDER BY fr.created_at DESC ")
            ) {
            stm.setInt(1, athleteId);

            List<FacilityReaction> result = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                FacilityReaction fr = new FacilityReaction();
                fr.setId(rs.getInt("id"));
                fr.setFacilityId(rs.getInt("facility_id"));
                fr.setFacilityName(rs.getString("facility_name"));
                fr.setComment(rs.getString("comment"));
                fr.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                fr.setOwnComment(true);
                result.add(fr);
            }
            return result;
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}