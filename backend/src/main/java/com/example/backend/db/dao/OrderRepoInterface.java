package com.example.backend.db.dao;

import java.time.LocalDate;
import java.util.List;

import com.example.backend.models.Message;
import com.example.backend.models.Orders;
import com.example.backend.models.helpers.EquipmentTurnoverDTO;

public interface OrderRepoInterface {
    
    public Message placeOrder(Orders o);
    public Message cancelActiveOrder(int orderId);

    public List<Orders> getAllOrders(int userId);

    public List<Orders> getAllOrdersForWorker();
    public Message updateOrderStatus(int orderId, String status);

    List<EquipmentTurnoverDTO> getEquipmentTurnover(LocalDate monthStart, LocalDate monthEnd);
}
