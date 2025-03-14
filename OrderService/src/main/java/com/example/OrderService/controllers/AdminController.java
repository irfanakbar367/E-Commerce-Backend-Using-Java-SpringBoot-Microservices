package com.example.OrderService.controllers;

import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/orders")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/updateOrder")
    public ResponseEntity<?> updateOrder(@RequestParam int orderId,
                                         @RequestParam OrderStatus orderStatus) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userRole = request.getHeader("X-User-Role");

        return adminService.updateOrder(orderId, orderStatus,userRole);
    }
}
