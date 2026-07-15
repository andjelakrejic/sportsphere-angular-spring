package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Equipment;
import com.example.backend.models.Message;

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

    @Override
    public Message addEquipment(Equipment e) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO equipment (name, sport_id, price, stock_quantity, image_url, description) " +
                "VALUES (?, ?, ?, ?, ?, ?) "
            );
        ) {
            stm.setString(1, e.getName());
            stm.setInt(2, e.getSportId());
            stm.setDouble(3, e.getPrice());
            stm.setInt(4, e.getStockQuantity());
            stm.setString(5, e.getImageUrl());
            stm.setString(6, e.getDescription());
            stm.executeUpdate();
            return new Message("Oprema je uspešno dodata.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Message(false, "Error when adding equipment");
        }
    }

    @Override
    public Message updateEquipment(Equipment e) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE equipment SET name = ?, sport_id = ?, price = ?, stock_quantity = ?, " +
                "image_url = ?, description = ? WHERE id = ? "
            );
        ) {
            stm.setString(1, e.getName());
            stm.setInt(2, e.getSportId());
            stm.setDouble(3, e.getPrice());
            stm.setInt(4, e.getStockQuantity());
            stm.setString(5, e.getImageUrl());
            stm.setString(6, e.getDescription());
            stm.setInt(7, e.getId());
            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message("Equipment successfully updated")
                : new Message(false,"Equipment not found");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new Message(false, "Error when updating equipment");
        }
    }
}
