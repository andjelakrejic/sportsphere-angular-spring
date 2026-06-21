package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Court;
public class CourtRepo implements CourtRepoInterface{

    @Override
    public List<Court> getAvailableCourts(int id) { // da se doda provera za sport tj join sa facility_sport??
        try (Connection conn = DB.source().getConnection();
         PreparedStatement stm = conn.prepareStatement("SELECT name, type FROM court WHERE facility_id=?")) {
         
            stm.setInt(1, id);
            List<Court> courts = new ArrayList<>();
            
            ResultSet rs = stm.executeQuery();
            while (rs.next()) { 
                int courtId = rs.getInt("id");
                int facilityId = rs.getInt("facility_id");
                int capacity = rs.getInt("capacity");
                String equipmentDescription = rs.getString(("equipment_description"));
                String name = rs.getString("name");
                String type = rs.getString("type");
                
                courts.add(new Court(courtId, facilityId, name, type, capacity, equipmentDescription)); 
            }
            return courts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public List<Court> getCourtsForFacility(int facilityId){
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT * FROM court WHERE facility_id = ?"
            );
        ) {
            stm.setInt(1, facilityId);

            List<Court> courts = new ArrayList<>();

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                courts.add(new Court(
                    rs.getInt("id"),
                    rs.getInt("facility_id"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("capacity"),
                    rs.getString("equipment_description")
                ));
            }
            return courts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
