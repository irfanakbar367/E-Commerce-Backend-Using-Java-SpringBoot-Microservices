package com.example.OrderService.services;

import com.example.Base_Domains.OrderNotifications.OrderEvent;
import com.example.Base_Domains.OrderNotifications.OrderEventDTO;
import com.example.OrderService.models.Orders;
import com.example.OrderService.repos.OrdersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final OrdersRepo ordersRepo;

    private final KafkaProducerService kafkaProducerService;

    public CustomerService(OrdersRepo ordersRepo, KafkaProducerService kafkaProducerService) {
        this.ordersRepo = ordersRepo;
        this.kafkaProducerService = kafkaProducerService;
    }

    public ResponseEntity<?> placeOrder(Orders order, String userRole) {
        if (!"USER".equals(userRole)) {
            return ResponseEntity.status(403).body("Access Denied: Only Customer can place order.");
        }
        try {
            OrderEventDTO orderEventDTO = new OrderEventDTO();
            orderEventDTO.setOrderDate(order.getOrderDate());
            orderEventDTO.setStatus(order.getStatus() != null ? order.getStatus().toString() : "");
            orderEventDTO.setUserId(order.getUserId());
            orderEventDTO.setProductIds(order.getProductIds());
            orderEventDTO.setTotalAmount(order.getTotalAmount());

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setStatus("Pending");
            orderEvent.setMessage("Your order is placed.");
            orderEvent.setOrderEventDTO(orderEventDTO);

            kafkaProducerService.sendMessage(orderEvent);
            return ResponseEntity.ok(ordersRepo.save(order));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Order is not placed. This is issue : " + e.getMessage());
        }
    }

    public ResponseEntity<?> seeMyOrders(int userId) {
        List<Orders> order = ordersRepo.findByUserId(userId);
        if (order != null && !order.isEmpty()) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You don't have any order placed.");
    }
}
