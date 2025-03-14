package com.example.OrderService.repos;

import com.example.OrderService.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepo extends JpaRepository<Orders, Integer> {

    List<Orders> findByUserId(int userId);
}
