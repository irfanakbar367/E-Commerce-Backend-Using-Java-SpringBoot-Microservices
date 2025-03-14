package com.example.OrderService.services;

import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.models.Orders;
import com.example.OrderService.repos.OrdersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private final OrdersRepo ordersRepo;

    public AdminService(OrdersRepo ordersRepo) {
        this.ordersRepo = ordersRepo;
    }

    public ResponseEntity<?> updateOrder(int orderId, OrderStatus orderStatus, String userRole) {
        if (!"ADMIN".equals(userRole)) {
            return ResponseEntity.status(403).body("Access Denied: Only Admin can update order.");
        }
        Orders order = ordersRepo.findById(orderId).orElse(null);
        if (order != null) {
            try {
                order.setStatus(orderStatus);
                return ResponseEntity.ok(ordersRepo.save(order));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Order is not updated. This is issue : " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Order with id : " + orderId + " is not found.");

    }
}
