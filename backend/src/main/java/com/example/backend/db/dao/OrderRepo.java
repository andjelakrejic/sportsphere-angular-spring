package com.example.backend.db.dao;

import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.db.DB;
import com.example.backend.models.EquipmentOrders;
import com.example.backend.models.Message;
import com.example.backend.models.Orders;
import com.example.backend.models.helpers.EquipmentTurnoverDTO;

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

    @Override
    public List<Orders> getAllOrdersForWorker() {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "SELECT o.id as order_id, o.athlete_id, o.total_price, o.status, o.created_at, " +
                "eo.id as item_id, eo.equipment_id, eo.quantity, eo.price_at_purchase " +
                "FROM orders o " +
                "JOIN equipment_orders eo ON eo.order_id = o.id " +
                "ORDER BY o.created_at DESC "
            );
        ) {
            Map<Integer, Orders> ordersMap = new LinkedHashMap<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Orders o = ordersMap.get(orderId);
                if (o == null) {
                    o = new Orders();
                    o.setId(orderId);
                    o.setAthleteId(rs.getInt("athlete_id"));
                    o.setPrice(rs.getDouble("total_price"));
                    o.setStatus(rs.getString("status"));
                    o.setCreatedAt(rs.getString("created_at"));
                    o.setItems(new ArrayList<>());
                    ordersMap.put(orderId, o);
                }
                EquipmentOrders item = new EquipmentOrders();
                item.setId(rs.getInt("item_id"));
                item.setOrderId(orderId);
                item.setEquipmentId(rs.getInt("equipment_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                o.getItems().add(item);
            }
            return new ArrayList<>(ordersMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message updateOrderStatus(int orderId, String status) {
        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(
                "UPDATE orders SET status = ? WHERE id = ? "
            );
        ) {
            stm.setString(1, status);
            stm.setInt(2, orderId);
            int rows = stm.executeUpdate();
            return rows > 0
                ? new Message("Order status successfully updated")
                : new Message(false, "Order not found");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(false, "Error when updating order");
        }
    }

    @Override
    public List<EquipmentTurnoverDTO> getEquipmentTurnover(LocalDate monthStart, LocalDate monthEnd) {
        List<EquipmentTurnoverDTO> result = new ArrayList<>();
        String sql =
            "SELECT e.name AS name, SUM(eo.quantity) AS qty, " +
            "SUM(eo.price_at_purchase * eo.quantity) AS revenue " +
            "FROM equipment_orders eo " +
            "JOIN equipment e ON e.id = eo.equipment_id " +
            "JOIN orders o ON o.id = eo.order_id " +
            "WHERE o.status = 'PICKED UP' " +
            "AND DATE(o.created_at) BETWEEN ? AND ? " +
            "GROUP BY e.id, e.name " +
            "ORDER BY revenue DESC";

        try (Connection conn = DB.source().getConnection();
            PreparedStatement stm = conn.prepareStatement(sql)) {

            stm.setDate(1, java.sql.Date.valueOf(monthStart));
            stm.setDate(2, java.sql.Date.valueOf(monthEnd));

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(new EquipmentTurnoverDTO(
                    rs.getString("name"),
                    rs.getInt("qty"),
                    rs.getDouble("revenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
