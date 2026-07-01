package com.example.backend.db.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.EquipmentOrders;
import com.example.backend.models.Message;
import com.example.backend.models.Orders;

public class OrderRepo implements OrderRepoInterface {

    @Override
    public Message placeOrder(Orders o) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection()) {

            // 1. Insert u orders
            PreparedStatement stm = conn.prepareStatement(
                "insert into orders (athlete_id, total_price, status, created_at) values (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );
            stm.setInt(1, o.getAthleteId());
            stm.setDouble(2, o.getPrice());
            stm.setString(3, "ORDERED");
            stm.setString(4, java.time.LocalDateTime.now().toString());
            stm.executeUpdate();

            // 2. Uzmi generisani order ID
            ResultSet keys = stm.getGeneratedKeys();
            if (!keys.next()) {
                m.setMessage("Error placing order");
                return m;
            }
            int orderId = keys.getInt(1);

            // 3. Insert svake stavke u equipment_orders
            for (EquipmentOrders item : o.getItems()) {
                PreparedStatement itemStm = conn.prepareStatement(
                    "insert into equipment_orders (order_id, equipment_id, quantity, price_at_purchase) " +
                    "values (?,?,?,?)"
                );
                itemStm.setInt(1, orderId);
                itemStm.setInt(2, item.getEquipmentId());
                itemStm.setInt(3, item.getQuantity());
                itemStm.setDouble(4, item.getPriceAtPurchase());
                itemStm.executeUpdate();
            }

            m.setMessage("Order placed");
            return m;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error placing order");
        return m;
    }

    @Override
    public Message cancelActiveOrder(int orderId) {
        Message m = new Message("");
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "update orders set status='CANCELED' where id=? and status='ORDERED'"
            )
        ) {
            stm.setInt(1, orderId);
            int linesAffected = stm.executeUpdate();
            if (linesAffected > 0) {
                m.setMessage("Order canceled");
                return m;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.setMessage("Error canceling order");
        return m;
    }

    @Override
    public List<Orders> getAllOrders(int userId) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement("select * from orders where athlete_id=?");
        ) {
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();

            List<Orders> orders = new ArrayList<>();

            while(rs.next()){
                Orders o = new Orders (
                    rs.getInt("id"),
                    rs.getInt("athlete_id"),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
                orders.add(o);
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
