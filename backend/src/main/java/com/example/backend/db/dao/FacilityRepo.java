package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.CourtInfo;
import com.example.backend.models.Facility;

public class FacilityRepo implements FacilityRepoInterface {

    @Override
    public List<Facility> getActiveFacilities() {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from facility where status='ACTIVE'");
        ) {
            List<Facility> facilities = new ArrayList<>();

            ResultSet rs = stm.executeQuery();

            while(rs.next()){
               // i konstruktor bez like_count
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );
                facilities.add(f);
            }
            return facilities;
        
       } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> getTop3Facilities() {
        List<Facility> facilities = new ArrayList<>();
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT f.*, COUNT(r.id) as like_count " +
                "FROM facility f " +
                "LEFT JOIN facility_reaction r ON f.id = r.facility_id AND r.type = 'LIKE' " +
                "WHERE f.status = 'ACTIVE' " +
                "GROUP BY f.id " +
                "ORDER BY like_count DESC " +
                "LIMIT 3"  // ovo mora biti ovde!
            );
        ) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),  // double
                    rs.getString("status"),          // String
                    rs.getInt("like_count")
                );
                facilities.add(f);
            }
            return facilities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // @Override
    // public int getNumOfLikes(int facilityId) {
    //     try (Connection conn = DB.source().getConnection();
    //         PreparedStatement stm = conn.prepareStatement(
    //             "SELECT COUNT(*) as like_count FROM facility_reaction " +
    //             "WHERE facility_id = ? AND type = 'LIKE'"
    //         );
    //     ) {
    //         stm.setInt(1, facilityId);
    //         ResultSet rs = stm.executeQuery();
    //         if (rs.next()) {
    //             return rs.getInt("like_count");
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return 0;
    // }
    
    @Override
    public List<String> getActiveCities() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT DISTINCT city FROM facility WHERE status='ACTIVE' ORDER BY city"
            );
        ) {
            List<String> cities = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
           
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
            return cities;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAllSports() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT DISTINCT name FROM sport ORDER BY name"
            );
        ) {
            List<String> sports = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                sports.add(rs.getString("name"));
            }
            return sports;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facility> searchFacilities(String name, String city, String sport, String type) {
        try (Connection conn = DB.source().getConnection()) {

            StringBuilder query = new StringBuilder(
                "SELECT DISTINCT f.* FROM facility f " +
                "LEFT JOIN facility_sport fs ON f.id = fs.facility_id " +
                "LEFT JOIN sport s ON fs.sport_id = s.id " +
                "LEFT JOIN court c ON f.id = c.facility_id " +
                "WHERE f.status = 'ACTIVE'"
            );

            if (name != null && !name.isEmpty())
                query.append(" AND f.name LIKE ?");
            if (city != null && !city.isEmpty())
                query.append(" AND f.city = ?");
            if (sport != null && !sport.isEmpty())
                query.append(" AND s.name = ?");
            if (type != null && !type.isEmpty())
                query.append(" AND c.type = ?");

            PreparedStatement stm = conn.prepareStatement(query.toString());

            List<Facility> facilities = new ArrayList<>();

            int index = 1;
            if (name != null && !name.isEmpty())
                stm.setString(index++, "%" + name + "%");
            if (city != null && !city.isEmpty())
                stm.setString(index++, city);
            if (sport != null && !sport.isEmpty())
                stm.setString(index++, sport);
            if (type != null && !type.isEmpty())
                stm.setString(index++, type);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Facility f = new Facility(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("working_hours_from"),
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"),
                    rs.getString("status")
                );
                facilities.add(f);
            }
            return facilities;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Facility getFacility(int id) {
         try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM facility WHERE id=?"
            );
        ) {
            stm.setInt(1, id);
            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Facility(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("city"),
                    rs.getString("address"), 
                    rs.getString("description"),
                    rs.getString("working_hours_from"), 
                    rs.getString("working_hours_to"),
                    rs.getDouble("price_per_hour"), 
                    rs.getString("status")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getFacilityImages(int id) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT image_url FROM facility_image WHERE facility_id = ?"
            );
        ) {
            stm.setInt(1, id);
            List<String> images = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("image_url"));
            }
            return images;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CourtInfo> getAvailableCourts(int id) { // da se doda provera za sport tj join sa facility_sport??
        try (Connection conn = DB.source().getConnection();
         PreparedStatement stm = conn.prepareStatement("SELECT name, type FROM court WHERE facility_id=?")) {
         
            stm.setInt(1, id);
            List<CourtInfo> courts = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                
                courts.add(new CourtInfo(name, type)); 
            }
            return courts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
