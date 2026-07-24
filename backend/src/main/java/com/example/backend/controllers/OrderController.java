package com.example.backend.controllers;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.OrderRepo;
import com.example.backend.models.Message;
import com.example.backend.models.Orders;
import com.example.backend.models.helpers.EquipmentTurnoverDTO;
import com.example.backend.services.ReportGenerator;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
   
    @GetMapping("/getAllOrders/{userId}")
    public List<Orders> getAllOrders(@PathVariable int userId) {
        return new OrderRepo().getAllOrders(userId);
    }

    @PostMapping("/placeOrder")
    public Message placeOrder(@RequestBody Orders o) {
        return new OrderRepo().placeOrder(o);
    }

    @PostMapping("/cancelActiveOrder/{orderId}")
    public Message cancelActiveOrder(@PathVariable int orderId) {
        return new OrderRepo().cancelActiveOrder(orderId);
    }

    @GetMapping("/getAllOrdersForWorker")
    public List<Orders> getAllOrdersForWorker() {
        return new OrderRepo().getAllOrdersForWorker();
    }

    // status ide kao query param (RequestParam), jer sadrži razmak ("PICKED UP")
    // pa ne može čisto kao PathVariable
    @PutMapping("/updateOrderStatus")
    public Message updateOrderStatus(@RequestParam int orderId, @RequestParam String status) {
        return new OrderRepo().updateOrderStatus(orderId, status);
    }

    @GetMapping("/reports/equipment")
    public ResponseEntity<byte[]> getEquipmentReport(@RequestParam String month) {
        try {
            YearMonth ym = YearMonth.parse(month);
            LocalDate monthStart = ym.atDay(1);
            LocalDate monthEnd = ym.atEndOfMonth();

            List<EquipmentTurnoverDTO> data = new OrderRepo().getEquipmentTurnover(monthStart, monthEnd);

            byte[] pdf = new ReportGenerator().generateEquipmentReport(monthStart, monthEnd, data);

            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=izvestaj-oprema.pdf")
                .body(pdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
