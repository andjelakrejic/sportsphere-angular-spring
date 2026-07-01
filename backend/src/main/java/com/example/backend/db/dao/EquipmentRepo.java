package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Equipment;

public class EquipmentRepo implements EquipmentRepoInterface{

    @Override
    public List<Equipment> getAllEquipment() {
       try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from equipment");
        ) {
            ResultSet rs = stm.executeQuery();

            List<Equipment> allEquipment = new ArrayList<>();

            while(rs.next()){
                Equipment e = new Equipment (
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("sport_id"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_url")
                );
                allEquipment.add(e);
            }
            return allEquipment;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Equipment> getEquipmentForSport(int sportId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from equipment where sport_id=?");
        ) {
            stm.setInt(1, sportId);
            ResultSet rs = stm.executeQuery();

            List<Equipment> allEquipment = new ArrayList<>();

            while(rs.next()){
                Equipment e = new Equipment (
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("sport_id"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getInt("stock_quantity"),
                    rs.getString("image_url")
                );
                allEquipment.add(e);
            }
            return allEquipment;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
